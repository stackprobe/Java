package charlotte.tools;

import java.util.HashMap;
import java.util.Map;

public class CrossMap<K, V> {
	private Map<K, V> _map = new HashMap<K, V>();
	private Map<V, K> _inv = new HashMap<V, K>();

	public CrossMap() {
		// noop
	}

	public void put(K key, V value) {
		_map.put(key, value);
		_inv.put(value, key);
	}

	public Map<K, V> map() {
		return _map;
	}

	public Map<V, K> inv() {
		return _inv;
	}
}
