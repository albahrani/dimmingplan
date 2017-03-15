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

import java.time.LocalTime;

import org.junit.Test;

public class DimmingPlanChannelTest {

	@Test
	public void testMiddle() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(6, 0), 0.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(8, 0), 100.0d);
		dimmingPlanChannel.interpolate();
		double value = dimmingPlanChannel.getValue(LocalTime.of(7, 0));
		assertEquals(50, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testBegin() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(6, 0), 80.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(22, 0), 0.0d);
		dimmingPlanChannel.interpolate();

		double value = dimmingPlanChannel.getValue(LocalTime.of(2, 0));
		assertEquals(40, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testEnd() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(2, 0), 0.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(22, 0), 100.0d);
		dimmingPlanChannel.interpolate();

		double value = dimmingPlanChannel.getValue(LocalTime.of(23, 0));
		assertEquals(75, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testForcedValue33() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(6, 0), 0.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(8, 0), 100.0d);
		dimmingPlanChannel.interpolate();

		dimmingPlanChannel.setForcedValue(33);
		double value = dimmingPlanChannel.getValue(LocalTime.of(8, 0));
		assertEquals(33, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testForcedValue25ThenClear() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(6, 0), 0.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(8, 0), 100.0d);
		dimmingPlanChannel.interpolate();

		dimmingPlanChannel.setForcedValue(25);
		dimmingPlanChannel.clearForcedValue();
		double value = dimmingPlanChannel.getValue(LocalTime.of(7, 0));
		assertEquals(50, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}

	@Test
	public void testNotInterpolated() {
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("ch1");
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(6, 0), 0.0d);
		dimmingPlanChannel.addTimetableEntry(LocalTime.of(8, 0), 100.0d);
		double value = dimmingPlanChannel.getValue(LocalTime.of(7, 0));
		assertEquals(0, value, 0.01);
		assertEquals("ch1", dimmingPlanChannel.getId());
	}
}
