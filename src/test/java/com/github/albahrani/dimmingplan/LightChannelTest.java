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

public class LightChannelTest {

	@Test
	public void testMiddle() {
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		double value = dimmingPlanChannel.getValue(longTime(7));
		assertEquals(50, value, 0.01);
		assertEquals("0x20", dimmingPlanChannel.getId());
		assertEquals("#000000", dimmingPlanChannel.getColor());
	}

	@Test
	public void testBegin() {
		long[] times = new long[] { longTime(6), longTime(22) };
		double[] lightPercentage = new double[] { 80, 0 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		double value = dimmingPlanChannel.getValue(longTime(2));
		assertEquals(40, value, 0.01);
	}

	@Test
	public void testEnd() {
		long[] times = new long[] { longTime(2), longTime(22) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		double value = dimmingPlanChannel.getValue(longTime(23));
		assertEquals(75, value, 0.01);
	}

	@Test
	public void testForcedValue33() {
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		dimmingPlanChannel.setForcedValue(33);
		double value = dimmingPlanChannel.getValue(longTime(8));
		assertEquals(33, value, 0.01);
	}

	@Test
	public void testForcedValue25ThenClear() {
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		dimmingPlanChannel.setForcedValue(25);
		dimmingPlanChannel.clearForcedValue();
		double value = dimmingPlanChannel.getValue(longTime(7));
		assertEquals(50, value, 0.01);
	}

	private static long longTime(int hour) {
		LocalTime lt = LocalTime.of(hour, 0);
		return lt.toNanoOfDay();
	}
}
