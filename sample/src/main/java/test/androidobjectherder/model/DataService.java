package test.androidobjectherder.model;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;


@EBean(scope = EBean.Scope.Singleton)
public class DataService {

	private List<DataObject> myData;

	@AfterInject
	void init() {
		myData = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			myData.add(new DataObject(i, "data: " + i));
		}
	}

	public List<DataObject> getMyData() {
		return myData;
	}
}
