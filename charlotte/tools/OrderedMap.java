package charlotte.tools;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OrderedMap<K, V> {
	private OrderedSet<K> _keyOrder;
	private Map<K, V> _map;

	public OrderedMap(Comparator<K> comp) {
		_keyOrder = new OrderedSet<K>(comp);
		_map = new TreeMap<K, V>(comp);
	}

	public void put(K key, V value) {
		_keyOrder.add(key);
		_map.put(key, value);
	}

	public void remove(K key) {
		_keyOrder.remove(key);
		_map.remove(key);
	}

	public int size() {
		return _map.size();
	}

	public Set<K> keySet() {
		return _map.keySet();
	}

	public List<K> keyOrder() {
		return _keyOrder.getList();
	}

	public V get(K key) {
		return _map.get(key);
	}

	public boolean containsKey(K key) {
		return _map.containsKey(key);
	}
}
