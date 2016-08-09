package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;

public class HugeQueue implements Closeable {
	private HugeFileQueue _writer;
	private HugeFileQueue _reader;

	public HugeQueue() {
		this(StringTools.CHARSET_UTF8);
	}

	public HugeQueue(String charset) {
		_writer = new HugeFileQueue(charset);
		_reader = new HugeFileQueue(charset);
	}

	public void add(String str) {
		_writer.add(str);
	}

	public void add(byte[] block) {
		_writer.add(block);
	}

	private void beforePoll() {
		if(_reader.size() == 0L && _writer.size() != 0L) {
			HugeFileQueue swap = _reader;
			_reader = _writer;
			_writer = swap;
			_writer.clear();
		}
	}

	public byte[] poll() {
		beforePoll();
		return _reader.poll();
	}

	public String pollString() {
		beforePoll();
		return _reader.pollString();
	}

	public long size() {
		return _writer.size() + _reader.size();
	}

	public void clear() {
		_writer.clear();
		_reader.clear();
	}

	@Override
	public void close() throws IOException {
		if(_writer != null) {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = null;
		}
	}
}
