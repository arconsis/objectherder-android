package test.androidobjectherder.other;

import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import test.androidobjectherder.model.DataObject;
import test.androidobjectherder.model.DataService;


public class OtherFragmentViewModel {

	private static final String        TAG     = OtherFragmentViewModel.class.getSimpleName();
	private static final AtomicInteger counter = new AtomicInteger();
	private final int              viewModelId;
	private       DataService      dataService;
	private       List<DataObject> myData;


	public OtherFragmentViewModel(DataService dataService) {
		viewModelId = counter.getAndIncrement();
		Log.w(TAG, "create " + viewModelId);

		this.dataService = dataService;
	}

	public String getViewModelId() {
		return "OtherFragmentViewModel:" + viewModelId;
	}


	public List<DataObject> getMyData() {
		if (myData == null) {
			Log.w(TAG, "loading Data in viewmodel" + viewModelId);
			myData = dataService.getMyData();
		}
		return myData;
	}
}
