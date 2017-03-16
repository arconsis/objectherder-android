package test.androidobjectherder.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidobjectherder.ObjectHerder;

import test.androidobjectherder.R;


@EActivity(R.layout.other_activity)
public class OtherActivity extends AppCompatActivity {


	private OtherActivityViewModel otherActivityViewModel;

	public static void start(Activity activity) {
		OtherActivity_.intent(activity).start();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		otherActivityViewModel = ObjectHerder.createOrReuseObject(this, null, OtherActivityViewModel::new);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.otherContent, OtherFragment.create(), "other").commit();
		}
	}

	@AfterViews
	void initViews() {
		setTitle(otherActivityViewModel.getText() + otherActivityViewModel.getViewModelId());
	}
}
