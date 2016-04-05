package charlotteLabo.mp4;

import charlotte.tools.ByteReader;

public abstract class BoxDetail {
	protected ByteReader _reader = null;

	public void load(Box box) {
		_reader = new ByteReader(box.image);
		load();
		_reader = null;
	}

	protected abstract void load();

	protected long readIntBE(int size) {
		return readInt(size, false);
	}

	protected long readIntLE(int size) {
		return readInt(size, true);
	}

	protected long readInt(int size, boolean le) {
		throw null; // TODO
	}
}
