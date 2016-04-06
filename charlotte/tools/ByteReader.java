package charlotte.tools;

public class ByteReader {
	private byte[] _src;
	private int _rPos;
	private int _endPos;

	public ByteReader(byte[] src) {
		this(src, 0);
	}

	public ByteReader(byte[] src, int rPos) {
		this(src, rPos, src.length - rPos);
	}

	public ByteReader(byte[] src, int rPos, int size) {
		if(src == null ||
				rPos < 0 ||
				size < 0 ||
				src.length < rPos ||
				src.length - rPos < size
				)
			throw new IllegalArgumentException();

		_src = src;
		_rPos = rPos;
		_endPos = rPos + size;
	}

	private static final byte DEF_CHR = 0x00;

	public int remaining() {
		return _endPos - _rPos;
	}

	public int read(byte[] buff, int wPos, int size) {
		if(buff == null ||
				wPos < 0 ||
				size < 0 ||
				buff.length < wPos ||
				buff.length - wPos < size
				)
			throw new IllegalArgumentException();

		int readSize = Math.min(size, remaining());
		ArrayTools.copy(_src, _rPos, buff, wPos, readSize);
		_rPos += readSize;
		return readSize;
	}

	public int read(byte[] buff, int wPos) {
		return read(buff, wPos, buff.length - wPos);
	}

	public int read(byte[] buff) {
		return read(buff, 0);
	}

	public byte[] read(int size) {
		if(size < 0 || remaining() < size) {
			throw new IllegalArgumentException();
		}
		byte[] buff = new byte[size];
		read(buff);
		return buff;
	}

	public int read() {
		if(_rPos < _endPos) {
			return _src[_rPos++] & 0xff;
		}
		return -1;
	}

	public void seek(int size) {
		_rPos += size;
	}
}
