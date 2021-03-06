package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderedSet<T> {
	private class Link {
		public T element;
		public Link prev;
		public Link next;
	}

	private Map<T, Link> _map;
	private Link _head;
	private Link _tail;

	public OrderedSet(Comparator<T> comp) {
		_map = new TreeMap<T, Link>(comp);
		_head = new Link();
		_tail = new Link();
		_head.next = _tail;
		_tail.prev = _head;
	}

	public void add(T element) {
		if(_map.containsKey(element) == false) {
			_map.put(element, _tail);
			_tail.element = element;
			_tail.next = new Link();
			_tail.next.prev = _tail;
			_tail = _tail.next;
		}
	}

	public void shift(T element) {
		if(_map.containsKey(element) == false) {
			_map.put(element, _head);
			_head.element = element;
			_head.prev = new Link();
			_head.prev.next = _head;
			_head = _head.prev;
		}
	}

	public void remove(T element) {
		if(_map.containsKey(element)) {
			Link link = _map.remove(element);
			link.prev.next = link.next;
			link.next.prev = link.prev;
		}
	}

	public int size() {
		return _map.size();
	}

	public Iterable<T> getIterable () {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					private Link _curr;

					private Iterator<T> init() {
						_curr = _head.next;
						return this;
					}

					@Override
					public boolean hasNext() {
						return _curr != _tail;
					}

					@Override
					public T next() {
						Link ret = _curr;
						_curr = _curr.next;
						return ret.element;
					}
				}
				.init();
			}
		};
	}

	public List<T> getList() {
		List<T> dest = new ArrayList<T>();

		for(T element : getIterable()) {
			dest.add(element);
		}
		return dest;
	}
	// old_same
	/*
	public List<T> getList() {
		List<T> dest = new ArrayList<T>();

		for(Link link = _head.next; link != _tail; link = link.next) {
			dest.add(link.element);
		}
		return dest;
	}
	*/
}
