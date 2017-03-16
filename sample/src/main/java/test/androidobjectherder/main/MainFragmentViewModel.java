package test.androidobjectherder.main;

import android.databinding.ObservableField;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import test.androidobjectherder.model.BeanService;


public class MainFragmentViewModel {
	private static final String                  TAG     = MainFragmentViewModel.class.getSimpleName();
	private static final AtomicInteger           counter = new AtomicInteger();
	public final         ObservableField<String> text    = new ObservableField<>("Hello");
	private final int viewModelId;
	private String paramID = "";
	private BeanService                   beanService;
	private Runnable                      navigateAction;
	private MainFragmentCallbackInterface callbackInterface;

	MainFragmentViewModel() {
		viewModelId = counter.getAndIncrement();
		Log.w(TAG, "create " + viewModelId + " paramID " + paramID);
	}

	public String getViewModelId() {
		return "MainFragmentViewModel:" + viewModelId + " paramID " + paramID;
	}


	public void setText(String text) {
		this.text.set(text);
		Log.i(TAG, "setText " + text + " MainFragmentViewModel:" + viewModelId);
	}

	public void onButtonClicked() {
		Log.i(TAG, "onButtonClicked MainFragmentViewModel:" + viewModelId);
		navigateAction.run();
	}

	public void onAnotherButtonClicked() {
		Log.i(TAG, "onAnotherButtonClicked MainFragmentViewModel:" + viewModelId);
		beanService.fancyBackground((string) -> {
			this.text.set(string);
			Log.i(TAG, "onAnotherButtonClicked received result from background MainFragmentViewModel:" + viewModelId);
		});
	}

	public void onContextActionButtonClicked() {
		Log.i(TAG, "onContextActionButtonClicked MainFragmentViewModel:" + viewModelId);
		callbackInterface.onActionWithContext();
	}

	public void setBeanService(BeanService beanService) {
		this.beanService = beanService;
		paramID = beanService.fancy();
	}

	public void setNavigateAction(Runnable navigateAction) {
		this.navigateAction = navigateAction;
	}

	public void setCallbackInterface(MainFragmentCallbackInterface callbackInterface) {
		this.callbackInterface = callbackInterface;
	}

}
