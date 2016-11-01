package charlotte.tools;

public interface ParamedGetter<K, V> {
	public V get(K key) throws Exception;
}
