package charlotte.tools;

public class RingList<T> {
	private T[] _buff;
	private int _head;

	public RingList(T[] bind_buff) {
		if(bind_buff.length == 0) {
			throw new IllegalArgumentException();
		}
		_buff = bind_buff;
	}

	public int size() {
		return _buff.length;
	}

	public T head() {
		return get(0);
	}

	public T tail() {
		return get(_buff.length - 1);
	}

	public T get(int index) {
		checkIndex(index);
		return _buff[(_head + index) % _buff.length];
	}

	public void set(int index, T element) {
		checkIndex(index);
		_buff[(_head + index) % _buff.length] = element;
	}

	private void checkIndex(int index) {
		if(index < 0 || _buff.length <= index) {
			throw new IndexOutOfBoundsException();
		}
	}

	public T shift(T element) {
		_head += _buff.length - 1;
		_head %= _buff.length;

		T ret = _buff[_head];
		_buff[_head] = element;

		return ret;
	}

	public T add(T element) {
		T ret = _buff[_head];
		_buff[_head] = element;

		_head++;
		_head %= _buff.length;

		return ret;
	}
}
