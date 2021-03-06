package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class ObjectList {
	private List<Object> _list = new ArrayList<Object>();

	public ObjectList() {
	}

	public ObjectList(List<?> list) {
		addAll(list);
	}

	public ObjectList(Object... arr) {
		addAll(arr);
	}

	public void addAll(List<?> list) {
		for(Object obj : list) {
			add(obj);
		}
	}

	public void addAll(Object[] arr) {
		for(Object obj : arr) {
			add(obj);
		}
	}

	public void add(Object obj) {
		_list.add(obj);
	}

	public int size() {
		return _list.size();
	}

	public List<Object> asList() {
		return _list;
	}

	public Object get(int index) {
		return _list.get(index);
	}

	public String getString(int index) {
		return getString(index, null);
	}

	public String getString(int index, String defval) {
		Object value = get(index);

		if(value == null) {
			return defval;
		}
		return value.toString();
	}

	public ObjectList getList(int index) {
		return (ObjectList)get(index);
	}

	public ObjectMap getMap(int index) {
		return (ObjectMap)get(index);
	}
}
