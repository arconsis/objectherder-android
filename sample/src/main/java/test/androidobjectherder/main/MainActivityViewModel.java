package test.androidobjectherder.main;

import android.util.Log;

import org.androidobjectherder.HerdedObjectLifecycle;

import java.util.concurrent.atomic.AtomicInteger;


class MainActivityViewModel implements HerdedObjectLifecycle {
	private static final AtomicInteger counter = new AtomicInteger();
	private static final String        TAG     = MainActivityViewModel.class.getSimpleName();
	private final int myId;

	MainActivityViewModel() {
		myId = counter.getAndIncrement();
		Log.w(TAG, "create " + myId);
	}

	String getTitle() {
		return " MainActivityViewModel " + myId;
	}

	@Override
	public void unherderd() {
		Log.w(TAG, "unherderd " + myId);
	}
}
