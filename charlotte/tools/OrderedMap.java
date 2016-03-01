package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OrderedMap<K, V> {
	private List<K> _keys = new ArrayList<K>();
	private List<V> _values = new ArrayList<V>();

	public void clear() {
		_keys.clear();
		_values.clear();
	}

	public void add(K key, V value) {
		_keys.add(key);
		_values.add(value);
	}

	public void add(Map<K, V> src) {
		for(K key : src.keySet()) {
			add(key, src.get(key));
		}
	}

	public List<K> keys() {
		return _keys;
	}

	public List<V> values() {
		return _values;
	}

	public void swap(int i, int j) {
		ArrayTools.swap(_keys, i, j);
		ArrayTools.swap(_values, i, j);
	}

	public List<Pair> getPairs() {
		List<Pair> dest = new ArrayList<Pair>();

		for(int index = 0; index < _keys.size(); index++) {
			Pair pair = new Pair();

			pair.key = _keys.get(index);
			pair.value = _values.get(index);

			dest.add(pair);
		}
		return dest;
	}

	public void setPairs(List<Pair> src) {
		clear();
		add(src);
	}

	private void add(List<Pair> src) {
		for(Pair pair : src) {
			add(pair);
		}
	}

	private void add(Pair src) {
		add(src.key, src.value);
	}

	public class Pair {
		public K key;
		public V value;
	}

	public void sort(Comparator<Pair> comp) {
		List<Pair> pairs = getPairs();
		ArrayTools.sort(pairs, comp);
		setPairs(pairs);
	}

	public void sortByKey(final Comparator<K> comp) {
		sort(new Comparator<Pair>() {
			@Override
			public int compare(Pair a, Pair b) {
				return comp.compare(a.key, b.key);
			}
		});
	}
}
