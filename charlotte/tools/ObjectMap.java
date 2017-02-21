package charlotte.tools;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectMap {
	private OrderedMap<String, Object> _map;

	public static ObjectMap create() {
		return new ObjectMap(StringTools.comp);
	}

	public static ObjectMap createIgnoreCase() {
		return new ObjectMap(StringTools.compIgnoreCase);
	}

	public ObjectMap() {
		this(StringTools.comp);
	}

	public ObjectMap(Comparator<String> comp) {
		_map = new OrderedMap<String, Object>(comp);
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

	public List<String> keyOrder() {
		return _map.keyOrder();
	}

	public Object get(String key) {
		return _map.get(key);
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defval) {
		Object value = get(key);

		if(value == null) {
			return defval;
		}
		return value.toString();
	}

	public ObjectList getList(String key) {
		return (ObjectList)get(key);
	}

	public ObjectMap getMap(String key) {
		return (ObjectMap)get(key);
	}
}
