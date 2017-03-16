/*
 * Copyright (C) 2017 The objectherder-android Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidobjectherder;

import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ObjectHerderTest {

	private ObjectHerder              objectHerder       = new ObjectHerder(new ObjectHerderLogger() {
		@Override
		public void log(String tag, String msg) {
			//ignored
		}
	});
	private String                    o1                 = "1";
	private String                    o2                 = "2";
	private TestObjectCreator<String> testObjectCreator1 = new TestObjectCreator<>(o1);
	private TestObjectCreator<String> testObjectCreator2 = new TestObjectCreator<>(o2);
	private TestActivity              fooactivity        = new TestActivity("fooactivity");
	private TestFragment              barFragment        = new TestFragment("barFragment");


	@Test
	public void create() {
		Object o = objectHerder.createOrReuse(fooactivity, null, testObjectCreator1);
		assertThat(o).isEqualTo(o1);
		assertThat(testObjectCreator1.wasCalled()).isTrue();
	}

	@Test
	public void createdOnlyOnce() {
		objectHerder.createOrReuse(fooactivity, null, testObjectCreator1);
		Object returned2 = objectHerder.createOrReuse(fooactivity, null, testObjectCreator2);
		assertThat(returned2).isEqualTo(o1);
		assertThat(testObjectCreator2.wasCalled()).isFalse();
	}

	@Test
	public void removedAndRecreated() {
		objectHerder.createOrReuse(fooactivity, null, testObjectCreator1);
		objectHerder.remove(fooactivity);
		Object o = objectHerder.createOrReuse(fooactivity, null, testObjectCreator1);
		assertThat(o).isEqualTo(o1);
		assertThat(testObjectCreator1.wasCalled()).isTrue();
	}

	@Test
	public void removedAndRecreatedForFragment() {
		objectHerder.createOrReuse(barFragment, null, testObjectCreator1);
		objectHerder.remove(barFragment);
		Object o = objectHerder.createOrReuse(barFragment, null, testObjectCreator1);
		assertThat(o).isEqualTo(o1);
		assertThat(testObjectCreator1.wasCalled()).isTrue();
	}

	@Test
	public void removedAndCreated() {
		objectHerder.remove(fooactivity);
		Object o = objectHerder.createOrReuse(fooactivity, null, testObjectCreator1);
		assertThat(o).isEqualTo(o1);
		assertThat(testObjectCreator1.wasCalled()).isTrue();
	}

	@Test
	public void createdAndRemoveNotified() {
		HerdedObjectLifecycle mock = Mockito.mock(HerdedObjectLifecycle.class);
		TestObjectCreator<HerdedObjectLifecycle> testObjectCreator = new TestObjectCreator<>(mock);
		objectHerder.createOrReuse(fooactivity, null, testObjectCreator);
		objectHerder.remove(fooactivity);
		verify(mock, times(1)).unherderd();
	}

}