package charlotte.tools;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HugeQueue implements Closeable {
	private FileQueue _writer;
	private FileQueue _reader;

	public HugeQueue() {
		_writer = new FileQueue();
		_writer.switchToWrite();
		_reader = new FileQueue();
	}

	public void add(String str) {
		try {
			add(str.getBytes(StringTools.CHARSET_UTF8));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void add(byte[] block) {
		_writer.add(block);
	}

	public byte[] poll() {
		if(_reader.size() == 0L) {
			if(_writer.size() == 0L) {
				return null;
			}
			FileQueue swap = _writer;
			_writer = _reader;
			_reader = swap;
			_writer.switchToWrite();
			_reader.switchToRead();
		}
		return _reader.poll();
	}

	public String pollString() {
		try {
			byte[] block = poll();

			if(block == null) {
				return null;
			}
			return new String(block, StringTools.CHARSET_UTF8);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public long size() {
		return _writer.size() + _reader.size();
	}

	public void clear() {
		_writer.switchToWrite();
		_writer._size = 0L;
		_reader.switchToWrite(); // _size == 0L なら poll() しない。
		_reader._size = 0L;
	}

	@Override
	public void close() throws IOException {
		if(_writer != null) {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = null;
		}
	}

	private static class FileQueue implements Closeable {
		private String _file = FileTools.makeTempPath();
		private FileOutputStream _writer;
		private FileInputStream _reader;
		private long _size = 0L;

		private void reset() {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = null;
			_reader = null;
		}

		public void switchToWrite() {
			try {
				reset();
				_writer = new FileOutputStream(_file);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public void add(byte[] block) {
			try {
				_writer.write(IntTools.toBytes(block.length));
				_writer.write(block);
				_size++;
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public void switchToRead() {
			try {
				reset();
				_reader = new FileInputStream(_file);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public byte[] poll() {
			_size--;

			return readBlock(
					IntTools.toInt(readBlock(4), 0)
					);
		}

		private byte[] readBlock(int size) {
			try {
				byte[] block = new byte[size];

				if(_reader.read(block) != size) {
					throw new RuntimeException("read error");
				}
				return block;
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public long size() {
			return _size;
		}

		@Override
		public void close() throws IOException {
			if(_file != null) {
				FileTools.close(_writer);
				FileTools.close(_reader);
				FileTools.del(_file);
				_file = null;
			}
		}
	}
}
