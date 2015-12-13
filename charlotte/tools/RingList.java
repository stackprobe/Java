package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class RingList<T> {
	private List<T> _list = new ArrayList<T>();
	private int _head;

	public RingList(int size) {
		while(_list.size() < size) {
			_list.add(null);
		}
		afterCtor();
	}

	public RingList(T[] list) {
		for(T element : list) {
			_list.add(element);
		}
		afterCtor();
	}

	private void afterCtor() {
		if(_list.size() < 1) {
			throw new IllegalArgumentException();
		}
	}

	public int size() {
		return _list.size();
	}

	public T head() {
		return get(0);
	}

	public T tail() {
		return get(_list.size() - 1);
	}

	public T get(int index) {
		checkIndex(index);
		return _list.get((_head + index) % _list.size());
	}

	public void set(int index, T element) {
		checkIndex(index);
		_list.set((_head + index) % _list.size(), element);
	}

	private void checkIndex(int index) {
		if(index < 0 || _list.size() <= index) {
			throw new IndexOutOfBoundsException();
		}
	}

	public T shift(T element) {
		_head += _list.size() - 1;
		_head %= _list.size();

		T ret = _list.get(_head);
		_list.set(_head, element);

		return ret;
	}

	public T add(T element) {
		T ret = _list.get(_head);
		_list.set(_head, element);

		_head++;
		_head %= _list.size();

		return ret;
	}
}
