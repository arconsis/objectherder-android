package test.androidobjectherder.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidobjectherder.ObjectHerder;

import test.androidobjectherder.R;


@EActivity(R.layout.main_activity)
public class MainActivity extends AppCompatActivity {


	private MainActivityViewModel mainActivityViewModel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivityViewModel = ObjectHerder.createOrReuseObject(this, null, MainActivityViewModel::new);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, MainFragment.create(), "main_fragemnt").commit();
		}
	}

	@AfterViews
	void initViews() {
		setTitle(mainActivityViewModel.getTitle());
	}
}
