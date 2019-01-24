package charlotte.tools;

import java.util.Arrays;
import java.util.List;

public class QueueData<T> {
	private LinkNode<T> _top;
	private LinkNode<T> _last;
	private int _count;

	public QueueData(T[] entries) {
		this(Arrays.asList(entries));
	}

	public QueueData(List<T> entries) {
		this();

		for(T entry : entries) {
			add(entry);
		}
	}

	public QueueData() {
		_top = new LinkNode<T>();
		_last = _top;
	}

	public void add(T element) {
		_last.element = element;
		_last.next = new LinkNode<T>();
		_last = _last.next;
		_count++;
	}

	public T poll() {
		if(_count <= 0) {
			return null;
		}
		T ret = _top.element;
		_top = _top.next;
		_count--;
		return ret;
	}

	public int size() {
		return _count;
	}

	private class LinkNode<U> {
		public U element;
		public LinkNode<U> next;
	}

	public void clear() {
		_top = new LinkNode<T>();
		_last = _top;
		_count = 0;
	}

	public ValueStore<T> toValueStore() {
		return new ValueStore<T>() {
			@Override
			public T get() {
				return poll();
			}

			@Override
			public void set(T element) {
				add(element);
			}
		};
	}
}
