package test.androidobjectherder.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidobjectherder.ObjectHerder;

import test.androidobjectherder.R;
import test.androidobjectherder.databinding.OtherFragmentBinding;
import test.androidobjectherder.model.DataService;


@EFragment
public class OtherFragment extends Fragment {
	private static final String TAG = OtherFragment.class.getSimpleName();
	@ViewById(R.id.recycler_view)
	RecyclerView recyclerView;
	@Bean
	DataService  dataService;
	private OtherFragmentViewModel viewModel;

	public static Fragment create() {
		return OtherFragment_.builder().build();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		OtherFragmentBinding otherFragmentBinding = OtherFragmentBinding.inflate(inflater, container, false);
		View view = otherFragmentBinding.getRoot();
		viewModel = ObjectHerder.createOrReuseObject(this, null, () -> {
			return new OtherFragmentViewModel(dataService);
		});
		otherFragmentBinding.setViewModel(viewModel);

		return view;
	}

	@AfterViews
	void init() {
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(new MyDataAdapter(viewModel.getMyData(),
												  (data) -> Toast.makeText(getContext(), "click on data " + data, Toast.LENGTH_LONG).show()));
		recyclerView.setHasFixedSize(true);


	}

}
