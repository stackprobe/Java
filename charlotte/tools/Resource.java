package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class Resource {
	private Object _src;

	public Resource(Object src) {
		if(src == null) {
			throw new NullPointerException("Resource format error");
		}
		_src = src;
	}

	public String getString(String key) {
		return toMap().getString(key).toString();
	}

	public String getString(int index) {
		return toList().getString(index).toString();
	}

	public Resource get(int index) {
		return new Resource(toList().get(index));
	}

	public Resource get(String key) {
		return new Resource(toMap().get(key));
	}

	public List<Resource> getList() {
		List<Resource> ret = new ArrayList<Resource>();

		for(Object src : toList().getList()) {
			ret.add(new Resource(src));
		}
		return ret;
	}

	private ObjectList toList() {
		return (ObjectList)_src;
	}

	private ObjectMap toMap() {
		return (ObjectMap)_src;
	}
}
