package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class OrderedMap<K, V> {
	private Map<K, Integer> _keyOrder;
	private int _keySerial;
	private Map<K, V> _map;

	public OrderedMap(Comparator<K> comp) {
		_keyOrder = new TreeMap<K, Integer>(comp);
		_keySerial = 0;
		_map = new TreeMap<K, V>(comp);
	}

	public void put(K key, V value) {
		if(_keyOrder.containsKey(key) == false) {
			_keyOrder.put(key, _keySerial);
			_keySerial++;
		}
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
		List<Map.Entry<K, Integer>> pairs = new ArrayList<Map.Entry<K, Integer>>();

		for(Map.Entry<K, Integer> pair : _keyOrder.entrySet()) {
			pairs.add(pair);
		}

		ArrayTools.sort(pairs, new Comparator<Map.Entry<K, Integer>>() {
			@Override
			public int compare(Entry<K, Integer> a, Entry<K, Integer> b) {
				return IntTools.comp.compare(a.getValue(), b.getValue());
			}
		});

		List<K> ret = new ArrayList<K>();

		for(Map.Entry<K, Integer> pair : pairs) {
			ret.add(pair.getKey());
		}
		return ret;
	}

	public V get(K key) {
		return _map.get(key);
	}
}
