package charlotte.tools;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * size() == 0 ならば、close(); しなくても良い。
 *
 */
public class HugeQueue implements Closeable {
	private FileQueueAC _writer;
	private FileQueueAC _reader;

	public HugeQueue() {
		_writer = new FileQueueAC();
		_reader = new FileQueueAC();
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
			FileQueueAC swap = _writer;
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
		_writer = new FileQueueAC();
		_reader = new FileQueueAC();
	}

	@Override
	public void close() throws IOException {
		if(_writer != null) {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = null;
		}
	}

	public static class FileQueueAC implements Closeable {
		private FileQueue _queue = null;

		public void add(String str) {
			beforeAdd();
			_queue.add(str);
		}

		public void add(byte[] block) {
			beforeAdd();
			_queue.add(block);
		}

		public String pollString() {
			if(_queue == null) {
				return null;
			}
			String ret = _queue.pollString();
			afterPoll();
			return ret;
		}

		public byte[] poll() {
			if(_queue == null) {
				return null;
			}
			byte[] ret = _queue.poll();
			afterPoll();
			return ret;
		}

		private void beforeAdd() {
			if(_queue == null) {
				_queue = new FileQueue();
			}
		}

		private void afterPoll() {
			if(_queue.size() == 0) {
				FileTools.close(_queue);
				_queue = null;
			}
		}

		public long size() {
			if(_queue == null) {
				return 0L;
			}
			return _queue.size();
		}

		@Override
		public void close() throws IOException {
			if(_queue != null) {
				FileTools.close(_queue);
				_queue = null;
			}
		}
	}

	public static class FileQueue implements Closeable {
		private String _file;
		private FileWriter _writer;
		private FileReader _reader;
		private long _size;

		public FileQueue() {
			this(FileTools.makeTempPath());
		}

		public FileQueue(String file) {
			_file = file;
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

		public void clear() {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = new FileWriter(_file);
			_reader = new FileReader(_file);
		}

		public void resetPoll() {
			FileTools.close(_reader);
			_reader = new FileReader(_file);
		}

		@Override
		public void close() throws IOException {
			if(_file != null) {
				FileTools.close(_writer);
				FileTools.close(_reader);
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

		public void add(byte[] block) {
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
				throw new RuntimeException("pollString error", e);
			}
		}

		public byte[] poll() {
			return read(IntTools.toInt(read(4), 0));
		}

		private byte[] read(int size) {
			try {
				byte[] block = new byte[size];

				if(_reader.read(block) != size) {
					throw new Exception("invalid return value");
				}
				return block;
			}
			catch(Exception e) {
				throw new RuntimeException("read error", e);
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

	public List<String> toStringList() {
		List<String> ret = new ArrayList<String>();

		for(; ; ) {
			String str = pollString();

			if(str == null) {
				break;
			}
			ret.add(str);
		}
		return ret;
	}

	public void sort(Comparator<byte[]> comp) throws Exception {
		new HugeQueueSorter() {
			@Override
			protected int comp(byte[] a, byte[] b) {
				return comp.compare(a, b);
			}
		}
		.mergeSort(this);
	}

	public void sortText(Comparator<String> comp) throws Exception {
		new HugeQueueSorter() {
			@Override
			protected int comp(byte[] a, byte[] b) {
				try {
					return comp.compare(
							new String(a, StringTools.CHARSET_UTF8),
							new String(b, StringTools.CHARSET_UTF8)
							);
				}
				catch(Throwable e) {
					throw RunnableEx.re(e);
				}
			}
		}
		.mergeSort(this);
	}

	public void sortText(HugeQueue hq) throws Exception {
		sortText(StringTools.comp);
	}

	public void sortTextIgnoreCase(HugeQueue hq) throws Exception {
		sortText(StringTools.compIgnoreCase);
	}
}
