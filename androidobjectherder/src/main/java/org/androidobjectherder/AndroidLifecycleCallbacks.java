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

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

/*
 * Listens to the Android ActivityLifecycle and FragmentLifecycle to notify the ObjectHerder
 *
 * @author Falk Appel
 */
class AndroidLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
	private static final String TAG = "ObjectHerderAndroidLifecycleCallbacks";
	private final ObjectHerder                               objectHerder;
	private final FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks;
	private final ObjectHerderLogger                         logger;

	AndroidLifecycleCallbacks(ObjectHerder objectHerder, ObjectHerderLogger objectHerderLogger) {
		this.objectHerder = objectHerder;
		fragmentLifecycleCallbacks = MyFragmentManager.createFragmentLifecycleCallbacks(objectHerder, objectHerderLogger);
		logger = objectHerderLogger;
	}

	@Override
	public void onActivityStopped(Activity activity) {
		AppCompatActivity appCompatActivity = toAppCompatActivity(activity);
		if (appCompatActivity == null) {
			return;
		}
		if (appCompatActivity.isFinishing()) {
			logger.log(TAG, "onActivityStopped finishing - removing " + appCompatActivity);
			objectHerder.remove(appCompatActivity);
		} else {
			logger.log(TAG, "onActivityStopped not finishing - no remove " + appCompatActivity);
		}
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle bundle) {
		AppCompatActivity appCompatActivity = toAppCompatActivity(activity);
		if (appCompatActivity == null) {
			return;
		}
		logger.log(TAG, "onActivityCreated " + activity + " adding FragmentLifecycleCallbacks");
		appCompatActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true);
	}

	@Nullable
	private AppCompatActivity toAppCompatActivity(Activity activity) {
		AppCompatActivity appCompatActivity;
		if (activity instanceof AppCompatActivity) {
			appCompatActivity = ((AppCompatActivity) activity);
		} else {
			return null;
		}
		return appCompatActivity;
	}

	@Override
	public void onActivityStarted(Activity activity) {
		//nothing to do
	}

	@Override
	public void onActivityResumed(Activity activity) {
		//nothing to do
	}

	@Override
	public void onActivityPaused(Activity activity) {
		//nothing to do
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
		//nothing to do
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		//nothing to do
	}

	// this is so sad :-( just to create an instance of FragmentLifecycleCallbacks
	private static class MyFragmentManager extends FragmentManager {

		private static FragmentLifecycleCallbacks createFragmentLifecycleCallbacks(ObjectHerder objectHerder, ObjectHerderLogger objectHerderLogger) {
			return new MyFragmentManager().createNewFragmentLifecycleCallbacks(objectHerder, objectHerderLogger);
		}

		private FragmentLifecycleCallbacks createNewFragmentLifecycleCallbacks(ObjectHerder objectHerder, ObjectHerderLogger objectHerderLogger) {
			return new MyFragmentLifecycleCallbacks(objectHerder, objectHerderLogger);
		}

		@Override
		public FragmentTransaction beginTransaction() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean executePendingTransactions() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Fragment findFragmentById(@IdRes int id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Fragment findFragmentByTag(String tag) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void popBackStack() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean popBackStackImmediate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void popBackStack(String name, int flags) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean popBackStackImmediate(String name, int flags) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void popBackStack(int id, int flags) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean popBackStackImmediate(int id, int flags) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getBackStackEntryCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public BackStackEntry getBackStackEntryAt(int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putFragment(Bundle bundle, String key, Fragment fragment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Fragment getFragment(Bundle bundle, String key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Fragment> getFragments() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isDestroyed() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb, boolean recursive) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks cb) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
			throw new UnsupportedOperationException();
		}

		private class MyFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {
			private final String            TAG             = "ObjectHerderFragmentLifecycleCallbacks";
			private final HashSet<Fragment> fragmentsToKeep = new HashSet<>();
			private final ObjectHerder       objectHerder;
			private final ObjectHerderLogger logger;

			private MyFragmentLifecycleCallbacks(ObjectHerder objectHerder, ObjectHerderLogger objectHerderLogger) {
				this.objectHerder = objectHerder;
				logger = objectHerderLogger;
			}

			@Override
			public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
				//nothing to do
			}

			@Override
			public void onFragmentStopped(FragmentManager fm, Fragment fragment) {
				logger.log(TAG, "onFragmentStopped");
				if (!fragmentsToKeep.remove(fragment)) {
					objectHerder.remove(fragment);
				}
			}

			@Override
			public void onFragmentSaveInstanceState(FragmentManager fm, Fragment fragment, Bundle outState) {
				logger.log(TAG, "onFragmentSaveInstanceState");
				fragmentsToKeep.add(fragment);
			}
		}
	}
}
