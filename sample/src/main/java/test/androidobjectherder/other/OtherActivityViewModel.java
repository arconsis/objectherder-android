package test.androidobjectherder.other;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import test.androidobjectherder.model.BeanService;


public class OtherActivityViewModel {

	private static final String        TAG     = OtherActivityViewModel.class.getSimpleName();
	private static final AtomicInteger counter = new AtomicInteger();
	private final int         viewModelId;
	private       BeanService beanService;

	private String text = "Hello";

	OtherActivityViewModel() {
		viewModelId = counter.getAndIncrement();
		Log.w(TAG, "create " + viewModelId);
	}

	public String getViewModelId() {
		return "OtherActivityViewModel:" + viewModelId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		Log.i(TAG, "setText " + text + " OtherActivityViewModel:" + viewModelId);
	}

	public void setBeanService(BeanService beanService) {
		this.beanService = beanService;
	}
}
