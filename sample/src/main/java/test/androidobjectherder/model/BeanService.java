package test.androidobjectherder.model;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import test.androidobjectherder.util.Consumer;

@EBean
public class BeanService {
	public String fancy() {
		return "Foobar";
	}

	@Background(delay = 2000)
	public void fancyBackground(Consumer<String> consumer) {
		consumer.consume("from background #" + Thread.currentThread().getId());
	}
}
