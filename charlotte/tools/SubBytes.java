package charlotte.tools;

public class SubBytes {
	private byte[] _block;
	private int _startPos;
	private int _size;

	public SubBytes(byte[] block) {
		this(block, 0);
	}

	public SubBytes(byte[] block, int startPos) {
		this(block, startPos, block.length - startPos);
	}

	public SubBytes(byte[] block, int startPos, int size) {
		if(block == null) {
			throw new NullPointerException();
		}
		if(startPos < 0 || block.length < startPos) {
			throw new IllegalArgumentException();
		}
		if(size < 0 || block.length - startPos < size) {
			throw new IllegalArgumentException();
		}
		_block = block;
		_startPos = startPos;
		_size = size;
	}

	public SubBytes(SubBytes block) {
		this(block, 0);
	}

	public SubBytes(SubBytes block, int startPos) {
		this(block, startPos, block._size - startPos);
	}

	public SubBytes(SubBytes block, int startPos, int size) {
		if(block == null) {
			throw new NullPointerException();
		}
		if(startPos < 0 || block._size < startPos) {
			throw new IllegalArgumentException();
		}
		if(size < 0 || block._size - startPos < size) {
			throw new IllegalArgumentException();
		}
		_block = block._block;
		_startPos = block._startPos + startPos;
		_size = size;
	}

	public byte[] getBlock() {
		return _block;
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
		return _block[_startPos + index];
	}

	public byte[] getBytes() {
		return ArrayTools.getBytes(_block, _startPos, _size);
	}
}
