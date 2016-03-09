package charlotte.satellite;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import charlotte.tools.StringTools;

public class ObjectMap {
	private Map<String, Object> _map;

	public ObjectMap() {
		this(StringTools.comp);
	}

	public static ObjectMap createIgnoreCase() {
		return new ObjectMap(StringTools.compIgnoreCase);
	}

	public ObjectMap(Comparator<String> comp) {
		_map = new TreeMap<String, Object>(comp);
	}

	public static ObjectMap create(Map<?, ?> map) {
		ObjectMap ret = new ObjectMap();
		ret.add(map);
		return ret;
	}

	public static ObjectMap createIgnoreCase(Map<?, ?> map) {
		ObjectMap ret = ObjectMap.createIgnoreCase();
		ret.add(map);
		return ret;
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

	public String getString(String key) {
		return (String)get(key);
	}

	public ObjectList getList(String key) {
		return (ObjectList)get(key);
	}

	public ObjectMap getMap(String key) {
		return (ObjectMap)get(key);
	}
}
