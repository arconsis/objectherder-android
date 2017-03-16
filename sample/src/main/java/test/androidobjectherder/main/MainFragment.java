package test.androidobjectherder.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidobjectherder.ObjectHerder;

import test.androidobjectherder.databinding.MainFragmentBinding;
import test.androidobjectherder.model.BeanService;
import test.androidobjectherder.other.OtherActivity;


@EFragment
public class MainFragment extends Fragment implements MainFragmentCallbackInterface {
	private static final String TAG = MainFragment.class.getSimpleName();

	@Bean
	BeanService beanService;
	private MainFragmentViewModel viewModel;

	public static Fragment create() {
		Bundle args = new Bundle();
		MainFragment mainFragment = new MainFragment_();
		mainFragment.setArguments(args);
		return mainFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		MainFragmentBinding mainFragmentBinding = MainFragmentBinding.inflate(inflater, container, false);
		View view = mainFragmentBinding.getRoot();
		viewModel = createOrReuseViewModel();
		mainFragmentBinding.setViewModel(viewModel);

		return view;
	}

	@AfterViews
	void init() {
		viewModel.setBeanService(beanService);
		viewModel.setNavigateAction(() -> OtherActivity.start(this.getActivity()));
	}

	private MainFragmentViewModel createOrReuseViewModel() {
		MainFragmentViewModel viewModel = ObjectHerder.createOrReuseObject(this, null, MainFragmentViewModel::new);
		viewModel.setCallbackInterface(this);
		return viewModel;
	}

	@Override
	public void onActionWithContext() {
		Toast.makeText(getContext(), "Toast with Context", Toast.LENGTH_SHORT).show();
	}
}
