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
		_reader = new FileQueue();
	}

	public void add(String str) {
		_writer.add(str);
	}

	public void add(byte[] block) {
		_writer.add(block);
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

	public byte[] poll() {
		if(_reader.size() == 0L) {
			if(_writer.size() == 0L) {
				return null;
			}
			FileQueue swap = _writer;
			_writer = _reader;
			_reader = swap;
		}
		return _reader.poll();
	}

	public long size() {
		return _writer.size() + _reader.size();
	}

	public void clear() {
		FileTools.close(_writer);
		FileTools.close(_reader);
		_writer = new FileQueue();
		_reader = new FileQueue();
	}

	@Override
	public void close() throws IOException {
		if(_writer != null) {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = null;
		}
	}

	public static class FileQueue implements Closeable {
		private String _file;
		private FileWriter _writer;
		private FileReader _reader;
		private long _size;

		public FileQueue() {
			_file = FileTools.makeTempPath();
			_writer = new FileWriter(_file);
			_reader = new FileReader(_file);
		}

		public void add(String str) {
			_writer.add(str);
			_size++;
		}

		public void add(byte[] block) {
			_writer.add(block);
			_size++;
		}

		public String pollString() {
			if(_size == 0L) {
				return null;
			}
			_size--;
			return _reader.pollString();
		}

		public byte[] poll() {
			if(_size == 0L) {
				return null;
			}
			_size--;
			return _reader.poll();
		}

		public long size() {
			return _size;
		}

		@Override
		public void close() throws IOException {
			if(_file != null) {
				FileTools.close(_reader);
				FileTools.close(_writer);
				FileTools.delete(_file);
				_file = null;
			}
		}
	}

	public static class FileWriter implements Closeable {
		private String _file;
		private FileOutputStream _writer;

		public FileWriter(String file) {
			this(file, false);
		}

		public FileWriter(String file, boolean append) {
			try {
				_file = file;
				_writer = new FileOutputStream(_file, append);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public void add(String str) {
			try {
				add(str.getBytes(StringTools.CHARSET_UTF8));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void add(byte[] block) {
			try {
				_writer.write(IntTools.toBytes(block.length));
				_writer.write(block);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void close() throws IOException {
			if(_writer != null) {
				FileTools.close(_writer);
				_writer = null;
			}
		}
	}

	public static class FileReader implements Closeable {
		private String _file;
		private FileInputStream _reader;

		public FileReader(String file) {
			try {
				_file = file;
				_reader = new FileInputStream(_file);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public String pollString() {
			try {
				return new String(poll(), StringTools.CHARSET_UTF8);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public byte[] poll() {
			return read(IntTools.toInt(read(4), 0));
		}

		private byte[] read(int size) {
			try {
				byte[] block = new byte[size];

				if(_reader.read(block) != size) {
					throw new Exception("read error");
				}
				return block;
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void close() throws IOException {
			if(_reader != null) {
				FileTools.close(_reader);
				_reader = null;
			}
		}
	}
}
