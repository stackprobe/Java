package charlotte.satellite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectMap {
	private Map<String, Object> _map = new HashMap<String, Object>();

	public ObjectMap() {
	}

	public ObjectMap(Map<?, ?> map) {
		add(map);
	}

	public void add(Map<?, ?> map) {
		for(Object key : map.keySet()) {
			add(key, map.get(key));
		}
	}

	public void add(Object key, Object value) {
		_map.put("" + key, value);
	}

	public int size() {
		return _map.size();
	}

	public Set<String> keySet() {
		return _map.keySet();
	}

	public Object get(String key) {
		return _map.get(key);
	}
}
