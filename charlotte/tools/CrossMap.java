package charlotte.tools;

import java.util.HashMap;
import java.util.Map;

public class CrossMap<K, V> {
	private Map<K, V> _kv = new HashMap<K, V>();
	private Map<V, K> _vk = new HashMap<V, K>();

	public CrossMap() {
		// noop
	}

	public void put(K key, V value) {
		_kv.put(key, value);
		_vk.put(value, key);
	}

	public Map<K, V> values() {
		return _kv;
	}

	public Map<V, K> keys() {
		return _vk;
	}
}
