package evergarden.labo.mp4;

import charlotte.tools.ArrayTools;
import charlotte.tools.ByteReader;

public abstract class BoxDetail {
	protected ByteReader _reader = null;

	public void load(Box box) {
		_reader = new ByteReader(box.image);
		load();
		_reader = null;
	}

	protected abstract void load();

	protected void seek(int size) {
		_reader.seek(size);
	}

	protected int readIntBE(int size) {
		return (int)readLongBE(size);
	}

	protected int readIntLE(int size) {
		return (int)readLongLE(size);
	}

	protected long readLongBE(int size) {
		return readLong(size, false);
	}

	protected long readLongLE(int size) {
		return readLong(size, true);
	}

	private long readLong(int size, boolean le) {
		byte[] buff = _reader.read(size);

		if(le) {
			ArrayTools.reverse(buff);
		}
		long value = 0L;

		for(byte chr : buff) {
			value <<= 8;
			value |= chr & 0xff;
		}
		return value;
	}
}
