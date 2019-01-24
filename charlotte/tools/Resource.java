package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class Resource {
	private Object _src;

	public Resource(Object src) {
		if(src == null) {
			throw new IllegalArgumentException("Resource has null");
		}
		_src = src;
	}

	private ObjectList cList() {
		return (ObjectList)_src;
	}

	private ObjectMap cMap() {
		return (ObjectMap)_src;
	}

	private String cString() {
		return (String)_src;
	}

	private byte[] cBytes() {
		return (byte[])_src;
	}

	public Resource get(int index) {
		return new Resource(cList().get(index));
	}

	public Resource get(String key) {
		return new Resource(cMap().get(key));
	}

	public List<Resource> getList() {
		List<Resource> ret = new ArrayList<Resource>();

		for(Object src : cList().asList()) {
			ret.add(new Resource(src));
		}
		return ret;
	}

	public String getString() {
		return cString();
	}

	public int getInt() {
		return Integer.parseInt(cString());
	}

	public long getLong() {
		return Long.parseLong(cString());
	}

	public double getDouble() {
		return Double.parseDouble(cString());
	}

	public byte[] getBytes() {
		return cBytes();
	}
}
