package test.androidobjectherder.other;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import test.androidobjectherder.databinding.DataItemBinding;
import test.androidobjectherder.model.DataObject;
import test.androidobjectherder.util.Consumer;


class MyDataAdapter extends RecyclerView.Adapter<MyDataViewHolder> {
	private final List<DataObject>     myData;
	private final Consumer<DataObject> onItemClickedAction;

	MyDataAdapter(List<DataObject> myData, Consumer<DataObject> onItemClickedAction) {
		this.myData = myData;
		this.onItemClickedAction = onItemClickedAction;
	}

	@Override
	public MyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		DataItemBinding dataItemBinding = DataItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		dataItemBinding.setDataItemViewModel(new DataItemViewModel(onItemClickedAction));
		return new MyDataViewHolder(dataItemBinding);
	}

	@Override
	public void onBindViewHolder(MyDataViewHolder holder, int position) {
		holder.bind(myData.get(position));
	}

	@Override
	public int getItemCount() {
		return myData.size();
	}

}
