package test.androidobjectherder;


import android.app.Application;
import android.util.Log;

import org.androidobjectherder.ObjectHerder;


public class LyfecycleTestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ObjectHerder.startHerding(this, Log::i);
	}
}
