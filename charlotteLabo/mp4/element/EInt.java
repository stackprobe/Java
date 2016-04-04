package charlotteLabo.mp4.element;

import charlotte.tools.ArrayTools;
import charlotte.tools.ByteReader;
import charlotte.tools.LongTools;
import charlotteLabo.mp4.Element;

public class EInt extends Element {
	private int _size;
	private boolean _le;

	public static EInt createBE(int size) {
		return new EInt(size, false);
	}

	public static EInt createLE(int size) {
		return new EInt(size, true);
	}

	public EInt(int size, boolean le) {
		_size = size;
		_le = le;
	}

	public void setSize(int size) {
		_size = size;
	}

	public void setLE(boolean le) {
		_le = le;
	}

	private long _value;

	@Override
	public void load(ByteReader reader) {
		byte[] raw = reader.read(_size);

		if(_le) {
			ArrayTools.reverse(raw);
		}
		_value = 0;

		for(byte chr : raw) {
			_value <<= 8;
			_value |= chr & 0xff;
		}
	}

	public long getValue() {
		return _value;
	}

	@Override
	public String getString() {
		return LongTools.toString0x(_value, _size * 2) + " (" + _value + ")";
	}
}
