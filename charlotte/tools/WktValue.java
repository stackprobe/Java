package charlotte.tools;

public class WktValue {
	private String _value;

	public WktValue(String value) {
		_value = value;
	}

	public String getValue() {
		return _value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
