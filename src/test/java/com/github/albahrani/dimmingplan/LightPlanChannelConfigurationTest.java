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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LightPlanChannelConfigurationTest {

	@Test
	public void testId() {
		DimmingPlanChannelConfiguration conf = new DimmingPlanChannelConfiguration();
		conf.setId("b");
		assertEquals("b", conf.getId());
	}

	@Test
	public void testColor() {
		DimmingPlanChannelConfiguration conf = new DimmingPlanChannelConfiguration();
		conf.setColor("a");
		assertEquals("a", conf.getColor());
	}

	@Test
	public void testTimetable() {
		DimmingPlanChannelConfiguration conf = new DimmingPlanChannelConfiguration();
		List<DimmingPlanTimeValuePair> timetable = new ArrayList<>();
		conf.setTimetable(timetable);
		assertEquals(timetable, conf.getTimetable());
	}
}
