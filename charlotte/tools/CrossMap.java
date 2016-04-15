package charlotte.tools;

import java.util.HashMap;
import java.util.Map;

public class CrossMap<K, V> {
	private Map<K, V> _kv;
	private Map<V, K> _vk;

	public static CrossMap<String, String> create() {
		return new CrossMap<String, String>(
				MapTools.<String>create(),
				MapTools.<String>create()
				);
	}

	public static CrossMap<String, String> createIgnoreCase() {
		return new CrossMap<String, String>(
				MapTools.<String>createIgnoreCase(),
				MapTools.<String>createIgnoreCase()
				);
	}

	public CrossMap() {
		this(new HashMap<K, V>(), new HashMap<V, K>());
	}

	public CrossMap(Map<K, V> kv, Map<V, K> vk) {
		_kv = kv;
		_vk = vk;
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
