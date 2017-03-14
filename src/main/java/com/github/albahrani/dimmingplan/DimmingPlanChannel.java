/**
 * Copyright © 2015 albahrani (https://github.com/albahrani)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.albahrani.dimmingplan;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariatePeriodicInterpolator;
import org.pmw.tinylog.Logger;

public class DimmingPlanChannel {

	private String id;
	private String color;
	private UnivariateFunction function;
	private long[] times;
	private double[] lightPercentage;

	private Double forcedValue = null;

	public DimmingPlanChannel(String id, String color, long[] times, double[] lightPercentage) {
		this.id = id;
		this.color = color;
		this.times = times;
		this.lightPercentage = lightPercentage;
		UnivariatePeriodicInterpolator upi = new UnivariatePeriodicInterpolator(new LinearInterpolator(), LocalTime.MAX.toNanoOfDay(), 1);

		double[] timesDouble = new double[times.length];
		for (int i = 0; i < times.length; i++) {
			timesDouble[i] = times[i];
		}
		function = upi.interpolate(timesDouble, lightPercentage);
	}

	public String getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public double getValue(double time) {
		if (this.forcedValue != null) {
			return this.forcedValue;
		}

		double value = this.function.value(time);
		return value;
	}

	public static DimmingPlanChannel load(DimmingPlanChannelConfiguration restChannel) {
		String channelId = restChannel.getId();
		String color = restChannel.getColor();

		List<DimmingPlanTimeValuePair> timetable = restChannel.getTimetable();
		if (timetable == null) {
			// skip this channel because there is no timetable
			Logger.warn("Skip channel {} because it has no timetable.", channelId);
			return null;
		}

		int numTimeValueEntries = timetable.size();
		long[] times = new long[numTimeValueEntries];
		double[] lightPercentage = new double[numTimeValueEntries];

		int i = 0;
		for (DimmingPlanTimeValuePair restTimeValuePair : timetable) {
			LocalTime time = restTimeValuePair.getTime();
			double perc = restTimeValuePair.getPerc();
			times[i] = time.toNanoOfDay();
			lightPercentage[i] = perc;
			i++;
		}

		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel(channelId, color, times, lightPercentage);
		return dimmingPlanChannel;
	}

	public DimmingPlanChannelConfiguration toConfiguration() {
		DimmingPlanChannelConfiguration retval = new DimmingPlanChannelConfiguration();
		retval.setColor(this.color);
		retval.setId(this.id);

		List<DimmingPlanTimeValuePair> timetable = new ArrayList<>();
		int timetableSize = this.getTimetableSize();
		for (int i = 0; i < timetableSize; i++) {
			DimmingPlanTimeValuePair tvpair = new DimmingPlanTimeValuePair();
			tvpair.setTime(this.getTimeFor(i));
			tvpair.setPerc(this.getLightPercentageFor(i));
			timetable.add(tvpair);
		}
		retval.setTimetable(timetable);

		return retval;
	}

	public void setForcedValue(double lightChannelValue) {
		this.forcedValue = lightChannelValue;

	}

	public void clearForcedValue() {
		this.forcedValue = null;
	}

	public Boolean isForced() {
		return (this.forcedValue != null);
	}

	private int getTimetableSize() {
		return this.times.length;
	}

	private LocalTime getTimeFor(int index) {
		return LocalTime.ofNanoOfDay(this.times[index]);
	}

	private double getLightPercentageFor(int index) {
		return this.lightPercentage[index];
	}
}
