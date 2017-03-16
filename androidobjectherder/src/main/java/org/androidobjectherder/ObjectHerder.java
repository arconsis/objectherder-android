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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.HashSet;


/**
 * I'm the ObjectHerder,  I herd your objects. I keep them if it is useful and I remove them if they are obsolete.
 *
 * Creates or reuses objects in the time an user expects an {@link AppCompatActivity} or a {@link Fragment} to be the same (later called scope).
 * In detail:
 * Objects are kept when:
 *  - Orientation is changed
 *  - the app goes to background and is resumed
 *  - another Activity ist started
 * Objects are removed when:
 *  - Activity is closed with back
 *  - a Fragment is removed
 *
 *  Register me in your {@link Application#onCreate()} with {@link #startHerding(Application)}.
 *
 * @author Falk Appel
 */
public class ObjectHerder {

	private static final String TAG = ObjectHerder.class.getSimpleName();
	private static ObjectHerder objectHerder;
	private final HashMap<String, HashSet<String>> combinedIdsByContextId = new HashMap<>();
	private final HashMap<String, Object>          objectsByCombinedId    = new HashMap<>();
	private ObjectHerderLogger logger;

	ObjectHerder(ObjectHerderLogger logger) {
		//only static access
		this.logger = logger;
	}

	/**
	 * Start the herding for the Application in {@link Application#onCreate()}
	 * @param application - the android application
	 */
	public static void startHerding(@NonNull Application application) {
		startHerding(application, new ObjectHerderLogger() {
			@Override
			public void log(String tag, String msg) {
				// don't log
			}
		});
	}

	/**
	 * Start the herding for the Application in {@link Application#onCreate()}
	 * @param application - the android application
	 * @param objectHerderLogger - your logger
	 */
	public static void startHerding(@NonNull Application application, @NonNull ObjectHerderLogger objectHerderLogger) {
		objectHerder = new ObjectHerder(objectHerderLogger);
		AndroidLifecycleCallbacks callbacks = new AndroidLifecycleCallbacks(objectHerder, objectHerderLogger);
		application.registerActivityLifecycleCallbacks(callbacks);
		objectHerderLogger.log(TAG, "registered");
	}


	/**
	 * Create or use a herded object
	 * @param activity - the context
	 * @param objectId - the object id, may be <code>null</code>
	 * @param objectCreator - callback to create the object if necessary
	 * @param <T> - the Objects type
	 * @return the herded object
	 */
	@NonNull
	public static <T> T createOrReuseObject(@NonNull AppCompatActivity activity, @Nullable String objectId, @NonNull ObjectCreator<T> objectCreator) {
		return getInstance().createOrReuse(activity, objectId, objectCreator);
	}

	/**
	 * Create or use a herded object
	 * @param fragment - the context
	 * @param objectId - the object id, may be <code>null</code>
	 * @param objectCreator - callback to create the object if necessary
	 * @param <T> - the Objects type
	 * @return the herded object
	 */
	@NonNull
	public static <T> T createOrReuseObject(@NonNull Fragment fragment, @Nullable String objectId, @NonNull ObjectCreator<T> objectCreator) {
		return getInstance().createOrReuse(fragment, objectId, objectCreator);
	}


	/**
	 * Get your ObjectHerder
	 * @return the started ObjectHerder
	 * @throws IllegalStateException if the Herder was not registered
	 */
	@NonNull
	public static ObjectHerder getInstance() {
		if (objectHerder == null) {
			throw new IllegalStateException(TAG + " must be registered in your Application.onCreate() method!");
		}
		return objectHerder;
	}

	/**
	 * Create or use a herded object
	 * @param activity - the context
	 * @param objectId - the object id, may be <code>null</code>
	 * @param objectCreator - callback to create the object if necessary
	 * @param <T> - the Objects type
	 * @return the herded object
	 */
	@NonNull
	public <T> T createOrReuse(@NonNull AppCompatActivity activity, @Nullable String objectId, @NonNull ObjectCreator<T> objectCreator) {
		return createOrReuse(toContextId(activity), objectId, objectCreator);
	}

	/**
	 * Create or use a herded object
	 * @param fragment - the context
	 * @param objectId - the object id, may be <code>null</code>
	 * @param objectCreator - callback to create the object if necessary
	 * @param <T> - the Objects type
	 * @return the herded object
	 */
	@NonNull
	public <T> T createOrReuse(@NonNull Fragment fragment, @Nullable String objectId, @NonNull ObjectCreator<T> objectCreator) {
		return createOrReuse(toContextId(fragment), objectId, objectCreator);
	}

	@NonNull
	private <T> T createOrReuse(@NonNull String contextId, @Nullable String objectId, @NonNull ObjectCreator<T> objectCreator) {
		String combinedId = combineIds(contextId, objectId);
		T found = getObject(combinedId);
		if (found != null) {
			logger.log(TAG, "reuse object contextId: " + contextId + " objectId: " + objectId);
			return found;
		} else {
			T created = objectCreator.create();
			HashSet<String> objectIds = combinedIdsByContextId.get(contextId);
			if (objectIds == null) {
				objectIds = new HashSet<>();
				combinedIdsByContextId.put(contextId, objectIds);
			}
			HashSet<String> combinedIds = objectIds;
			combinedIds.add(combinedId);
			objectsByCombinedId.put(combinedId, created);
			logger.log(TAG, "created object contextId: " + contextId + " objectId: " + objectId);
			return created;
		}
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private <T> T getObject(@NonNull String combinedId) {
		return (T) objectsByCombinedId.get(combinedId);
	}

	@NonNull
	private String combineIds(@NonNull String contextId, @Nullable String objectId) {
		StringBuilder builder = new StringBuilder(contextId).append(':');
		if (objectId != null) {
			builder.append(objectId);
		}
		return builder.toString();
	}

	void remove(@NonNull AppCompatActivity activity) {
		removeContext(toContextId(activity));
	}

	void remove(@NonNull Fragment fragment) {
		removeContext(toContextId(fragment));
	}

	private void removeContext(@NonNull String contextId) {
		HashSet<String> combinedIds = combinedIdsByContextId.get(contextId);
		if (combinedIds == null) {
			logger.log(TAG, "removed contextId: " + contextId + "  - no objects");
			return;
		}
		for (String combinedId : combinedIds) {
			Object remove = objectsByCombinedId.remove(combinedId);
			if (remove != null) {
				if (remove instanceof HerdedObjectLifecycle) {
					logger.log(TAG, "removed object combinedId: " + combinedId + " with lifecycle");
					((HerdedObjectLifecycle) remove).unherderd();
				} else {
					logger.log(TAG, "removed object combinedId: " + combinedId);
				}
			} else {
				logger.log(TAG, "no registered object for combinedId: " + combinedId);
			}
		}
	}

	// TODO: check this id generation - perhaps create a HasId interface?
	@NonNull
	private String toContextId(@NonNull Activity activity) {
		return activity.getClass().getName() + ":" + activity.getTaskId();
	}

	// TODO: check this id generation - perhaps create a HasId interface?
	@NonNull
	private String toContextId(@NonNull Fragment fragment) {
		return fragment.getClass().getName() + ":" + fragment.getId();
	}

}
