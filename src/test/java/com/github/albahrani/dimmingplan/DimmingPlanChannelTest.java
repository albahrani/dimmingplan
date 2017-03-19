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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.OptionalDouble;

import org.junit.Test;

public class DimmingPlanChannelTest {

	@Test
	public void testEmptyChannel() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create();
		assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(7, 0))).isEmpty();
		dimmingPlanChannel.interpolate();
		assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(7, 0))).isEmpty();
	}

	@Test
	public void testChannelWithSingleDefine() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 37.0d).interpolate();
		for (int i = 0; i < 24; i++) {
			assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(i, 0))).hasValueCloseTo(37.0d, within(0.001d));
		}
	}

	@Test
	public void testChannelOverWriteDefine() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 37.0d).interpolate();
		dimmingPlanChannel.define(LocalTime.of(6, 0), 73.0d);

		assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(3, 0))).hasValueCloseTo(73.0d, within(0.001d));
	}

	@Test
	public void testChannelUndefineEmpty() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 37.0d).undefine(LocalTime.of(6, 0)).interpolate();
		assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(6, 0))).isEmpty();
	}

	@Test
	public void testChannelOverWriteDefineWithEmpty() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 37.0d).interpolate();
		dimmingPlanChannel.undefine(LocalTime.of(6, 0));
		assertThat(dimmingPlanChannel.getPercentage(LocalTime.of(6, 0))).isEmpty();
	}

	@Test
	public void testMiddle() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d).interpolate();
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(7, 0));
		assertThat(value).hasValueCloseTo(50.0d, within(0.001d));
	}

	@Test
	public void testBegin() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 80.0d).define(LocalTime.of(22, 0), 0.0d).interpolate();
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(2, 0));
		assertThat(value).hasValueCloseTo(40.0d, within(0.001d));
	}

	@Test
	public void testEnd() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(2, 0), 0.0d).define(LocalTime.of(22, 0), 100.0d).interpolate();
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(23, 0));
		assertThat(value).hasValueCloseTo(75.0d, within(0.001d));
	}

	@Test
	public void testForcedValue33() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d).interpolate();
		dimmingPlanChannel.pin(33.0d);
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(8, 0));
		assertTrue(dimmingPlanChannel.isPinned());
		assertThat(value).hasValueCloseTo(33.0d, within(0.001d));
	}

	@Test
	public void testForcedValue25ThenClear() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d).interpolate();
		dimmingPlanChannel.pin(25.0d);
		assertTrue(dimmingPlanChannel.isPinned());
		dimmingPlanChannel.unpin();
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(7, 0));
		assertThat(value).hasValueCloseTo(50.0d, within(0.001d));
		assertFalse(dimmingPlanChannel.isPinned());
	}

	@Test
	public void testNotInterpolated() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d);
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(7, 0));
		assertThat(value).hasValueCloseTo(50.0d, within(0.001d));
	}

	@Test
	public void testInterpolateThenChange() {
		DimmingPlanChannel dimmingPlanChannel = DimmingPlanChannel.create().define(LocalTime.of(6, 0), 0.0d).define(LocalTime.of(8, 0), 100.0d).interpolate();
		OptionalDouble value = dimmingPlanChannel.getPercentage(LocalTime.of(7, 0));
		assertThat(value).hasValueCloseTo(50.0d, within(0.001d));

		dimmingPlanChannel.define(LocalTime.of(7, 0), 80.0d);
		OptionalDouble value2 = dimmingPlanChannel.getPercentage(LocalTime.of(7, 0));
		assertThat(value2).hasValueCloseTo(80.0d, within(0.001d));
	}
}
