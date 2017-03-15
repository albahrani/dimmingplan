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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.List;

import org.junit.Test;

public class DimmingPlanTest {

	@Test
	public void testPlanLoad() {
		DimmingPlan plan = new DimmingPlan();
		plan.addTimetableEntry("ch1", LocalTime.of(6, 0), 0.0d);
		plan.addTimetableEntry("ch1", LocalTime.of(8, 0), 100.0d);
		plan.interpolate();

		List<DimmingPlanChannel> dimmingPlanChannels = plan.getChannels();
		assertNotNull(dimmingPlanChannels);
		assertEquals(1, dimmingPlanChannels.size());
		DimmingPlanChannel dimmingPlanChannel = dimmingPlanChannels.get(0);
		assertNotNull(dimmingPlanChannel);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testSetForcedValue() {
		DimmingPlan plan = new DimmingPlan();
		plan.addTimetableEntry("ch1", LocalTime.of(6, 0), 0.0d);
		plan.addTimetableEntry("ch1", LocalTime.of(8, 0), 100.0d);
		plan.interpolate();

		plan.setForcedValue("ch1", 10.0d);

		assertTrue(plan.isForced("ch1"));
		assertEquals(10.0d, plan.getValue("ch1", LocalTime.of(8, 0)), 0.00001d);
	}

	@Test
	public void testSetForcedValueUnknownChannel() {
		DimmingPlan plan = new DimmingPlan();
		plan.addTimetableEntry("ch1", LocalTime.of(6, 0), 0.0d);
		plan.addTimetableEntry("ch1", LocalTime.of(8, 0), 100.0d);
		plan.interpolate();

		assertNull(plan.getValue("chX", LocalTime.of(8, 0)));

		plan.setForcedValue("chX", 10.0d);

		assertFalse(plan.isForced("ch1"));
		assertEquals(100.0d, plan.getValue("ch1", LocalTime.of(8, 0)), 0.00001d);
	}

	@Test
	public void testClearForcedValue() {
		DimmingPlan plan = new DimmingPlan();
		plan.addTimetableEntry("ch1", LocalTime.of(6, 0), 0.0d);
		plan.addTimetableEntry("ch1", LocalTime.of(8, 0), 100.0d);
		plan.interpolate();

		assertFalse(plan.isForced("ch1"));
		assertEquals(50.0d, plan.getValue("ch1", LocalTime.of(7, 0)), 0.00001d);

		plan.setForcedValue("ch1", 10.0d);

		assertTrue(plan.isForced("ch1"));
		assertEquals(10.0d, plan.getValue("ch1", LocalTime.of(7, 0)), 0.00001d);

		plan.clearForcedValue("ch1");

		assertFalse(plan.isForced("ch1"));
		assertEquals(50.0d, plan.getValue("ch1", LocalTime.of(7, 0)), 0.00001d);
	}
}
