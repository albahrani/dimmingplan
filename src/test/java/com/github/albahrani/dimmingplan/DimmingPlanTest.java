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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.Set;

import org.junit.Test;

public class DimmingPlanTest {

	@Test
	public void testPlanLoad() {
		DimmingPlan plan = DimmingPlan.create();
		plan.channel("ch1").define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d);
		plan.interpolate();

		assertEquals(1, plan.getChannelAmount());
		Set<String> channelNames = plan.getChannelNames();
		assertNotNull(channelNames);
		assertTrue(channelNames.contains("ch1"));

		assertThat(plan.channel("ch1").getPercentage(LocalTime.of(6, 0))).hasValueCloseTo(0.0d, within(0.001d));
		assertThat(plan.channel("ch1").getPercentage(LocalTime.of(7, 0))).hasValueCloseTo(50.0d, within(0.001d));
		assertThat(plan.channel("ch1").getPercentage(LocalTime.of(8, 0))).hasValueCloseTo(100.0d, within(0.001d));

		plan.removeChannel("ch1");
		assertEquals(0, plan.getChannelAmount());
	}
}
