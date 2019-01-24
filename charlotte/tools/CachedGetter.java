package charlotte.tools;

import java.util.Map;
import java.util.Set;

public class CachedGetter<K, V> implements ParamedGetter<K, V> {
	public static <V> CachedGetter<String, V> create(ParamedGetter<String, V> getter) {
		return new CachedGetter<String, V>(MapTools.<V>create(), getter);
	}

	public static <V> CachedGetter<String, V> createIgnoreCase(ParamedGetter<String, V> getter) {
		return new CachedGetter<String, V>(MapTools.<V>createIgnoreCase(), getter);
	}

	private Map<K, V> _cache;
	private ParamedGetter<K, V> _getter;

	public CachedGetter(Map<K, V> cache, ParamedGetter<K, V> getter) {
		_cache = cache;
		_getter = getter;
	}

	@Override
	public V get(K key) {
		V value = _cache.get(key);

		if(value == null) {
			value = _getter.get(key);

			if(value == null) {
				throw new RuntimeException("Getter returned null");
			}
			_cache.put(key, value);
		}
		return value;
	}

	public Set<K> keySet() {
		return _cache.keySet();
	}
}
