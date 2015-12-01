package charlotte.tools;

public class JsonValue {
	private String _value;

	public JsonValue(String value) {
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
