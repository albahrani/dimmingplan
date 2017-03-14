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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LightPlanTest {

	@Test
	public void testPlanLoad() {
		DimmingPlanConfiguration configuration = mock(DimmingPlanConfiguration.class);
		List<DimmingPlanChannelConfiguration> configChannels = new ArrayList<>();
		DimmingPlanChannelConfiguration configChannel = mock(DimmingPlanChannelConfiguration.class);
		when(configChannel.getId()).thenReturn("0x20");
		List<DimmingPlanTimeValuePair> configTimetable = new ArrayList<>();
		DimmingPlanTimeValuePair configTTE = mock(DimmingPlanTimeValuePair.class);
		when(configTTE.getTime()).thenReturn(LocalTime.of(6, 0));
		when(configTTE.getPerc()).thenReturn(0.0d);
		configTimetable.add(configTTE);
		DimmingPlanTimeValuePair configChannel2 = mock(DimmingPlanTimeValuePair.class);
		when(configChannel2.getTime()).thenReturn(LocalTime.of(8, 0));
		when(configChannel2.getPerc()).thenReturn(100.0d);
		configTimetable.add(configChannel2);
		when(configChannel.getTimetable()).thenReturn(configTimetable);
		configChannels.add(configChannel);
		when(configuration.getChannels()).thenReturn(configChannels);

		DimmingPlan plan = new DimmingPlan();
		plan.load(configuration);

		List<DimmingPlanChannel> dimmingPlanChannels = plan.getChannels();
		assertNotNull(dimmingPlanChannels);
		assertEquals(1, dimmingPlanChannels.size());
		DimmingPlanChannel dimmingPlanChannel = dimmingPlanChannels.get(0);
		assertNotNull(dimmingPlanChannel);
		assertEquals("0x20", dimmingPlanChannel.getId());
	}

	@Test
	public void testToConfiguration() {
		DimmingPlanConfiguration configuration = mock(DimmingPlanConfiguration.class);
		List<DimmingPlanChannelConfiguration> configChannels = new ArrayList<>();
		DimmingPlanChannelConfiguration configChannel = mock(DimmingPlanChannelConfiguration.class);
		when(configChannel.getId()).thenReturn("0x20");
		when(configChannel.getColor()).thenReturn("#000000");
		List<DimmingPlanTimeValuePair> configTimetable = new ArrayList<>();
		DimmingPlanTimeValuePair configTTE = mock(DimmingPlanTimeValuePair.class);
		when(configTTE.getTime()).thenReturn(LocalTime.of(6, 0));
		when(configTTE.getPerc()).thenReturn(0.0d);
		configTimetable.add(configTTE);
		DimmingPlanTimeValuePair configTTE2 = mock(DimmingPlanTimeValuePair.class);
		when(configTTE2.getTime()).thenReturn(LocalTime.of(8, 0));
		when(configTTE2.getPerc()).thenReturn(100.0d);
		configTimetable.add(configTTE2);
		when(configChannel.getTimetable()).thenReturn(configTimetable);
		configChannels.add(configChannel);
		when(configuration.getChannels()).thenReturn(configChannels);

		DimmingPlan plan = new DimmingPlan();
		plan.load(configuration);

		DimmingPlanConfiguration conf = plan.toConfiguration();
		assertNotNull(conf);
		List<DimmingPlanChannelConfiguration> channels = conf.getChannels();
		assertNotNull(channels);
		assertEquals(1, channels.size());
		DimmingPlanChannelConfiguration lightChannel = channels.get(0);
		assertNotNull(lightChannel);
		assertEquals("0x20", lightChannel.getId());
		assertEquals("#000000", lightChannel.getColor());
		List<DimmingPlanTimeValuePair> timetable = lightChannel.getTimetable();
		assertNotNull(timetable);
		assertEquals(2, timetable.size());
		DimmingPlanTimeValuePair tte1 = timetable.get(0);
		assertNotNull(tte1);
		assertEquals(LocalTime.of(6, 0), tte1.getTime());
		assertEquals(0.0d, tte1.getPerc(), 0.0001d);

		DimmingPlanTimeValuePair tte2 = timetable.get(1);
		assertNotNull(tte2);
		assertEquals(LocalTime.of(8, 0), tte2.getTime());
		assertEquals(100.0d, tte2.getPerc(), 0.0001d);
	}

	@Test
	public void testSetForcedValue() {
		DimmingPlan plan = new DimmingPlan();
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		plan.addChannel(dimmingPlanChannel);

		plan.setForcedValue("0x20", 10.0d);

		assertTrue(plan.isForced("0x20"));
		assertEquals(10.0d, plan.getValue("0x20", longTime(8)), 0.00001d);
	}

	@Test
	public void testSetForcedValueUnknownChannel() {
		DimmingPlan plan = new DimmingPlan();
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		plan.addChannel(dimmingPlanChannel);

		plan.setForcedValue("0x21", 10.0d);

		assertFalse(plan.isForced("0x20"));
		assertEquals(100.0d, plan.getValue("0x20", longTime(8)), 0.00001d);
	}

	@Test
	public void testClearForcedValue() {
		DimmingPlan plan = new DimmingPlan();
		long[] times = new long[] { longTime(6), longTime(8) };
		double[] lightPercentage = new double[] { 0, 100 };
		DimmingPlanChannel dimmingPlanChannel = new DimmingPlanChannel("0x20", "#000000", times, lightPercentage);
		plan.addChannel(dimmingPlanChannel);

		assertFalse(plan.isForced("0x20"));
		assertEquals(50.0d, plan.getValue("0x20", longTime(7)), 0.00001d);

		plan.setForcedValue("0x20", 10.0d);

		assertTrue(plan.isForced("0x20"));
		assertEquals(10.0d, plan.getValue("0x20", longTime(7)), 0.00001d);

		plan.clearForcedValue("0x20");

		assertFalse(plan.isForced("0x20"));
		assertEquals(50.0d, plan.getValue("0x20", longTime(7)), 0.00001d);
	}

	private static long longTime(int hour) {
		LocalTime lt = LocalTime.of(hour, 0);
		return lt.toNanoOfDay();
	}
}
