package charlotte.tools;

public class ByteBuffer {
	private BlockBuffer _buff = new BlockBuffer();

	private int _nextBlockSize = 1024;
	private byte[] _block;
	private int _startPos;
	private int _index;

	public ByteBuffer() {
	}

	public void add(byte chr) {
		if(_block == null) {
			_block = new byte[_nextBlockSize];
			_nextBlockSize += 1024;
		}
		_block[_index] = chr;
		_index++;

		if(_block.length <= _index) {
			flush();
			_block = null;
			_startPos = 0;
			_index = 0;
		}
	}

	public void bindAdd(byte[] block) {
		flush();
		_buff.bindAdd(block);
	}

	public void bindAdd(byte[] block, int startPos) {
		flush();
		_buff.bindAdd(block, startPos);
	}

	public void bindAdd(byte[] block, int startPos, int size) {
		flush();
		_buff.bindAdd(block, startPos, size);
	}

	public byte[] getBytes() {
		flush();
		return _buff.getBytes();
	}

	public BlockBuffer directGetBuff() {
		flush();
		return _buff;
	}

	public void flush() {
		if(_startPos < _index) {
			_buff.bindAdd(_block, _startPos, _index - _startPos);
			_startPos = _index;
		}
	}
}
