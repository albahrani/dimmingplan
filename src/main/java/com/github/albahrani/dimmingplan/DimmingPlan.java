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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DimmingPlan {

	private Map<String, DimmingPlanChannel> dimmingPlanChannels = new HashMap<>();

	public DimmingPlan() {

	}

	public DimmingPlan addTimetableEntry(String channelId, LocalTime time, double percentage) {
		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.get(channelId);
		if (dimmingPlanChannel == null) {
			dimmingPlanChannel = new DimmingPlanChannel(channelId);
			this.dimmingPlanChannels.put(channelId, dimmingPlanChannel);
		}

		dimmingPlanChannel.addTimetableEntry(time, percentage);
		return this;
	}

	public void interpolate() {
		Set<Entry<String, DimmingPlanChannel>> entrySet = this.dimmingPlanChannels.entrySet();
		for (Entry<String, DimmingPlanChannel> entry : entrySet) {
			DimmingPlanChannel value = entry.getValue();
			value.interpolate();
		}
	}

	public void setForcedValue(String channelId, double channelValue) {
		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.get(channelId);
		if (dimmingPlanChannel != null) {
			dimmingPlanChannel.setForcedValue(channelValue);
		}
	}

	public void clearForcedValue(String channelId) {
		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.get(channelId);
		if (dimmingPlanChannel != null) {
			dimmingPlanChannel.clearForcedValue();
		}
	}

	public Boolean isForced(String channelId) {
		Boolean forced = null;

		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.get(channelId);
		if (dimmingPlanChannel != null) {
			forced = dimmingPlanChannel.isForced();
		}

		return forced;
	}

	public Double getValue(String channelId, LocalTime time) {
		DimmingPlanChannel dimmingPlanChannel = this.dimmingPlanChannels.get(channelId);
		if (dimmingPlanChannel != null) {
			return dimmingPlanChannel.getValue(time);
		}
		return null;
	}

	public List<DimmingPlanChannel> getChannels() {
		return new ArrayList<>(this.dimmingPlanChannels.values());
	}
}
