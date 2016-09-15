package charlotte.tools;

public class ValueBox<T> implements ValueGetter<T>, ValueSetter<T> {
	private T _value;

	public ValueBox() {
		this(null);
	}

	public ValueBox(T value) {
		set(value);
	}

	@Override
	public T get() throws Exception {
		return _value;
	}

	@Override
	public void set(T value) {
		_value = value;
	}
}
