package test.androidobjectherder.other;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import test.androidobjectherder.databinding.DataItemBinding;
import test.androidobjectherder.model.DataObject;
import test.androidobjectherder.util.Bindable;


public class MyDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Bindable<DataObject> {
	public final DataItemBinding dataItemBinding;

	public MyDataViewHolder(DataItemBinding dataItemBinding) {
		super(dataItemBinding.getRoot());
		this.dataItemBinding = dataItemBinding;
		itemView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dataItemBinding.getDataItemViewModel().onClick();
	}

	@Override
	public void bind(DataObject toBind) {
		dataItemBinding.getDataItemViewModel().bind(toBind);
	}
}
