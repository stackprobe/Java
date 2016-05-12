package charlotte.tools;

public class SubBytes {
	private byte[] _master;
	private int _startPos;
	private int _size;

	public SubBytes(byte[] master) {
		this(master, 0);
	}

	public SubBytes(byte[] master, int startPos) {
		this(master, startPos, master.length - startPos);
	}

	public SubBytes(byte[] master, int startPos, int size) {
		if(master == null) {
			throw new NullPointerException();
		}
		if(startPos < 0 || master.length < startPos) {
			throw new IllegalArgumentException();
		}
		if(size < 0 || master.length - startPos < size) {
			throw new IllegalArgumentException();
		}
		_master = master;
		_startPos = startPos;
		_size = size;
	}

	public SubBytes(SubBytes master) {
		this(master, 0);
	}

	public SubBytes(SubBytes master, int startPos) {
		this(master, startPos, master._size - startPos);
	}

	public SubBytes(SubBytes master, int startPos, int size) {
		if(master == null) {
			throw new NullPointerException();
		}
		if(startPos < 0 || master._size < startPos) {
			throw new IllegalArgumentException();
		}
		if(size < 0 || master._size - startPos < size) {
			throw new IllegalArgumentException();
		}
		_master = master._master;
		_startPos = master._startPos + startPos;
		_size = size;
	}

	public byte[] getMaster() {
		return _master;
	}

	public int getStartPos() {
		return _startPos;
	}

	public int size() {
		return _size;
	}

	public byte get(int index) {
		if(index < 0 || _size <= index) {
			throw new IllegalArgumentException();
		}
		return _master[_startPos + index];
	}

	public byte[] getBytes() {
		return ArrayTools.getBytes(_master, _startPos, _size);
	}
}
