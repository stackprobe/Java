package charlotte.tools;

public class ValueBox<T> implements ValueStore<T> {
	private T _value;

	public ValueBox() {
		this(null);
	}

	public ValueBox(T value) {
		set(value);
	}

	@Override
	public T get() {
		return _value;
	}

	@Override
	public void set(T value) {
		_value = value;
	}
}
