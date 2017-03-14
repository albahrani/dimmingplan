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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DimmingPlan {

	private List<DimmingPlanChannel> dimmingPlanChannels = new ArrayList<>();

	public DimmingPlan() {

	}

	public List<DimmingPlanChannel> getChannels() {
		return this.dimmingPlanChannels;
	}

	public void addChannel(DimmingPlanChannel dimmingPlanChannel) {
		this.dimmingPlanChannels.add(dimmingPlanChannel);
	}

	public void load(DimmingPlanConfiguration configuration) {
		this.dimmingPlanChannels.clear();

		List<DimmingPlanChannelConfiguration> channels = configuration.getChannels();
		for (DimmingPlanChannelConfiguration channelConfiguration : channels) {
			DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.load(channelConfiguration);
			if (dimmingPlanChannel != null) {
				this.dimmingPlanChannels.add(dimmingPlanChannel);
			}
		}
	}

	public DimmingPlanConfiguration toConfiguration() {
		DimmingPlanConfiguration configuration = new DimmingPlanConfiguration();
		List<DimmingPlanChannelConfiguration> channelConfigurations = new ArrayList<>();
		for (DimmingPlanChannel dimmingPlanChannel : this.dimmingPlanChannels) {
			DimmingPlanChannelConfiguration channelConfiguration = dimmingPlanChannel.toConfiguration();
			channelConfigurations.add(channelConfiguration);
		}
		configuration.setChannels(channelConfigurations);
		return configuration;
	}

	public void setForcedValue(String channelId, double channelValue) {
		for (Iterator<DimmingPlanChannel> iterator = dimmingPlanChannels.iterator(); iterator.hasNext();) {
			DimmingPlanChannel dimmingPlanChannel = iterator.next();
			if (channelId.equals(dimmingPlanChannel.getId())) {
				dimmingPlanChannel.setForcedValue(channelValue);
				break;
			}
		}
	}

	public void clearForcedValue(String channelId) {
		for (Iterator<DimmingPlanChannel> iterator = dimmingPlanChannels.iterator(); iterator.hasNext();) {
			DimmingPlanChannel dimmingPlanChannel = iterator.next();
			if (channelId.equals(dimmingPlanChannel.getId())) {
				dimmingPlanChannel.clearForcedValue();
				break;
			}
		}
	}

	public Boolean isForced(String channelId) {
		Boolean forced = null;

		for (Iterator<DimmingPlanChannel> iterator = dimmingPlanChannels.iterator(); iterator.hasNext();) {
			DimmingPlanChannel dimmingPlanChannel = iterator.next();
			if (channelId.equals(dimmingPlanChannel.getId())) {
				forced = dimmingPlanChannel.isForced();
				break;
			}
		}

		return forced;
	}

	public Double getValue(String channelId, double time) {
		for (Iterator<DimmingPlanChannel> iterator = dimmingPlanChannels.iterator(); iterator.hasNext();) {
			DimmingPlanChannel dimmingPlanChannel = iterator.next();
			if (channelId.equals(dimmingPlanChannel.getId())) {
				return dimmingPlanChannel.getValue(time);
			}
		}
		return null;
	}
}
