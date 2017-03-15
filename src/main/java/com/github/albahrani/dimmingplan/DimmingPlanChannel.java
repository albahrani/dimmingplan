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

public class DimmingPlanChannel {

	private String id;
	private UnivariateFunction function = null;
	private List<DimmingPlanTimeValuePair> times = new ArrayList<>();

	private Double forcedValue = null;

	public DimmingPlanChannel(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public double getValue(LocalTime time) {
		return this.getValue(time.toNanoOfDay());
	}

	public double getValue(double time) {
		if (this.forcedValue != null) {
			return this.forcedValue;
		}

		if (this.function == null) {
			return 0.0d;
		}
		double value = this.function.value(time);
		return value;
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

	public DimmingPlanChannel addTimetableEntry(LocalTime time, double percentage) {
		DimmingPlanTimeValuePair tvp = new DimmingPlanTimeValuePair();
		tvp.setTime(time);
		tvp.setPerc(percentage);
		this.times.add(tvp);
		return this;
	}

	public void interpolate() {
		UnivariatePeriodicInterpolator upi = new UnivariatePeriodicInterpolator(new LinearInterpolator(), LocalTime.MAX.toNanoOfDay(), 1);

		int timesSize = this.times.size();

		double[] timesDouble = new double[timesSize];
		double[] lightDouble = new double[timesSize];

		for (int i = 0; i < timesSize; i++) {
			DimmingPlanTimeValuePair timeValuePair = times.get(i);
			LocalTime time = timeValuePair.getTime();
			double perc = timeValuePair.getPerc();
			timesDouble[i] = time.toNanoOfDay();
			lightDouble[i] = perc;
		}

		function = upi.interpolate(timesDouble, lightDouble);
	}
}
