package test.androidobjectherder.other;

import android.databinding.ObservableField;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import test.androidobjectherder.model.DataObject;
import test.androidobjectherder.util.Bindable;
import test.androidobjectherder.util.Consumer;


public class DataItemViewModel implements Bindable<DataObject> {

	private static final AtomicInteger counter = new AtomicInteger();
	private static final String        TAG     = DataItemViewModel.class.getSimpleName();
	private final int viewModelId;
	public ObservableField<String> text = new ObservableField<>("");
	private Consumer<DataObject> onItemClickedAction;
	private DataObject           dataObject;

	public DataItemViewModel(Consumer<DataObject> onItemClickedAction) {
		this.onItemClickedAction = onItemClickedAction;
		viewModelId = counter.getAndIncrement();
		Log.w(TAG, "create " + viewModelId);
	}


	public void onClick() {
		onItemClickedAction.consume(dataObject);
	}

	@Override
	public void bind(DataObject toBind) {
		dataObject = toBind;
		text.set(dataObject.getName() + " viewmodel: " + viewModelId);
	}
}
