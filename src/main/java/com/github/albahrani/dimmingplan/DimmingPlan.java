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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * <h1>DimmingPlan</h1>
 * <p>
 * A dimming plan allows you to define a continuous and interpolated timetable
 * for one day. you are then able to retrieve the according percentage value for
 * each point in time you need.
 * </p>
 * <p>
 * You may define several {@link DimmingPlanChannel}s with each following its
 * own definitions.
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
 * The {@link DimmingPlan} is filled {@link #channel(String)} method to create a
 * new or access an existing channel. It must be completed by calling
 * {@link #interpolate()} <b>at least once</b>. It is no problem calling it
 * multiple times or even changing the plan again, but it always has to be
 * accepted by calling {@link #interpolate()} afterwards.
 * </p>
 * 
 * <h1>Example:</h1>
 * 
 * <pre>
 * <code>
 * DimmingPlan plan = DimmingPlan.create();
 * plan.channel("ch1").define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d);
 * plan.interpolate();
 * </code>
 * ...
 * <code>
 * plan.channel("ch1").getValue(LocalTime.of(6, 0));
 * </code>
 * </pre>
 * 
 * @author albahrani
 *
 */
public class DimmingPlan {

	private Map<String, DimmingPlanChannel> dimmingPlanChannels = new HashMap<>();

	/**
	 * Use {@link #create()} instead.
	 */
	private DimmingPlan() {
		// private
	}

	/**
	 * Method used to create a new empty dimming plan.
	 * 
	 * @return the new empty dimming plan
	 */
	public static DimmingPlan create() {
		return new DimmingPlan();
	}

	/**
	 * Add a channel with a certain name to the dimming plan. If a channel with
	 * this name is already registered, it will be reused.
	 * 
	 * @param channelId
	 *            the channels name, the definition should take place for.
	 *            <b><code>null</code> is not allowed!</b>
	 * @return the {@link DimmingPlanChannel} instance, for fluent API support
	 */
	public DimmingPlanChannel channel(String channelId) {
		Objects.requireNonNull(channelId);
		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.computeIfAbsent(channelId, id -> {
			return DimmingPlanChannel.create();
		});
		return dimmingPlanChannel;
	}

	/**
	 * Removes a channel from the dimming plan. If channel has not been added,
	 * nothing is done.
	 * 
	 * @param channelId
	 *            the name of the channel to be removed. <b><code>null</code> is
	 *            not allowed!</b>
	 * @return the {@link DimmingPlan} instance, for fluent API support
	 */
	public DimmingPlan removeChannel(String channelId) {
		Objects.requireNonNull(channelId);
		this.dimmingPlanChannels.remove(channelId);
		return this;
	}

	/**
	 * Force a recalculation of the interpolated intermediate percentage values
	 * based on the currently defined timetable for all channels of the dimming
	 * plan.
	 * 
	 * @return the {@link DimmingPlan} instance, for fluent API support
	 */
	public DimmingPlan interpolate() {
		Set<Entry<String, DimmingPlanChannel>> entrySet = this.dimmingPlanChannels.entrySet();
		for (Entry<String, DimmingPlanChannel> entry : entrySet) {
			DimmingPlanChannel value = entry.getValue();
			value.interpolate();
		}
		return this;
	}

	/**
	 * Returns the names of the channels defined for the plan.
	 * 
	 * @return a set of channel names
	 */
	public Set<String> getChannelNames() {
		return new HashSet<>(this.dimmingPlanChannels.keySet());
	}

	/**
	 * Returns the amount of channels available in this plan.
	 * 
	 * @return the amount of channels
	 */
	public int getChannelAmount() {
		return this.dimmingPlanChannels.size();
	}
}
