package test.androidobjectherder.model;


public class DataObject {
	private long   id;
	private String name;

	public DataObject() {
	}

	public DataObject(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DataObject{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
