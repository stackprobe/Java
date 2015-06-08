package charlotte.satellite;

import java.util.ArrayList;
import java.util.List;

public class ObjectList {
	private List<Object> _list = new ArrayList<Object>();

	public ObjectList() {
	}

	public ObjectList(List<?> list) {
		add(list);
	}

	public void add(List<?> list) {
		for(Object obj : list) {
			add(obj);
		}
	}

	public void add(Object obj) {
		_list.add(obj);
	}

	public int size() {
		return _list.size();
	}

	public List<Object> getList() {
		return _list;
	}

	public Object get(int index) {
		return _list.get(index);
	}
}
