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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariatePeriodicInterpolator;

/**
 * <h1>DimmingPlanChannel</h1>
 * <p>
 * A DimmingPlanChannel is a distinct timeline within the DimmingPlan.
 * </p>
 * <p>
 * A {@link DimmingPlanChannel} predefines several times {@link LocalTime} with
 * certain percentage values <i>0.0d - 100.0d</i>, for the times in between the
 * percentage values are interpolated.
 * </p>
 * <p>
 * Since the {@link DimmingPlanChannel} currently is only supporting a daily
 * schedule, it also interpolates between the last defined time of day and the
 * first time of day.
 * </p>
 * <p>
 * A DimmingPlanChannel is directly associated to a {@link DimmingPlan},so
 * channels are managed via {@link DimmingPlan#channel(String)} and
 * {@link DimmingPlan#removeChannel(String)}.
 * </p>
 * 
 * @author Sir-Aliman
 *
 */
public class DimmingPlanChannel {

	private UnivariateFunction function = null;
	private TreeMap<LocalTime, Double> times = new TreeMap<>();

	private OptionalDouble forcedValue = OptionalDouble.empty();

	/**
	 * use {@link DimmingPlan#channel(String)} instead
	 */
	private DimmingPlanChannel() {
		// private
	}

	/**
	 * Creates a dimming plan channel. Should not be used directly, use
	 * {@link DimmingPlan#channel(String)} instead.
	 * 
	 * @return a new {@link DimmingPlanChannel} instance
	 */
	protected static DimmingPlanChannel create() {
		return new DimmingPlanChannel();
	}

	/**
	 * Returns the percentage for a certain time.
	 * 
	 * @param timeObj
	 *            a {@link LocalTime} instance, the percentage shall be
	 *            retrieved for
	 * @return an {@link OptionalDouble} which holds the percentage value or is
	 *         {@link OptionalDouble#empty()} if executed for an empty channel.
	 */
	public OptionalDouble getPercentage(LocalTime timeObj) {
		Objects.requireNonNull(timeObj);
		double time = timeObj.toNanoOfDay();

		if (this.function == null) {
			if (this.times.size() > 0) {
				this.recalculate();
			} else {
				return OptionalDouble.empty();
			}
		}
		double value = this.forcedValue.orElse(this.function.value(time));
		return OptionalDouble.of(value);
	}

	/**
	 * Pins the channel to a certain value. Overwrite the percentage of the
	 * timetable untill {@link #unpin()} is called.
	 * 
	 * @param percentage
	 *            the constant value for this channel
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel pin(Double percentage) {
		Objects.requireNonNull(percentage);
		this.forcedValue = OptionalDouble.of(percentage);
		return this;
	}

	/**
	 * Unpin the channel. Fall back to the percentage according to the
	 * timetable.
	 * 
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel unpin() {
		this.forcedValue = OptionalDouble.empty();
		return this;
	}

	/**
	 * Check if a channel is pinned.
	 * 
	 * @return {@link Boolean#TRUE} if pinned, {@link Boolean#FALSE} otherwise
	 */
	public Boolean isPinned() {
		return this.forcedValue.isPresent();
	}

	/**
	 * Define a percentage value for a certain point in time. The time is
	 * respected in calculating the interpolated intermediate percentage values.
	 * 
	 * @param time
	 *            the {@link LocalTime} for which a percentage shall be defined
	 * @param percentage
	 *            the percentage that shall be defined
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel define(LocalTime time, double percentage) {
		Objects.requireNonNull(time);
		this.times.put(time, percentage);
		if (this.function != null) {
			this.recalculate();
		}
		return this;
	}

	/**
	 * Removes a defined time/percentage pair from the timetable.
	 * 
	 * @param time
	 *            the {@link LocalTime} which shall be undefined
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel undefine(LocalTime time) {
		Objects.requireNonNull(time);
		this.times.remove(time);
		if (this.function != null) {
			this.recalculate();
		}
		return this;
	}

	/**
	 * Force a recalculation of the interpolated intermediate percentage values
	 * based on the currently defined timetable.
	 * 
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel interpolate() {
		return this.recalculate();
	}

	private DimmingPlanChannel recalculate() {
		int timesSize = this.times.size();

		if (timesSize < 1) {
			this.function = null;
			return this;
		}

		UnivariatePeriodicInterpolator upi = new UnivariatePeriodicInterpolator(new LinearInterpolator(), LocalTime.MAX.toNanoOfDay(), 1);

		double[] timesDouble = new double[timesSize];
		double[] lightDouble = new double[timesSize];

		Set<Entry<LocalTime, Double>> entrySet = this.times.entrySet();
		Iterator<Entry<LocalTime, Double>> iterator = entrySet.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Map.Entry<LocalTime, Double> entry = iterator.next();
			LocalTime time = entry.getKey();
			double perc = entry.getValue();
			timesDouble[i] = time.toNanoOfDay();
			lightDouble[i] = perc;
			i++;
		}

		function = upi.interpolate(timesDouble, lightDouble);
		return this;
	}
}
