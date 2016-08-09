package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;

public class HugeQueue implements Closeable {
	private HugeQueueSw _writer;
	private HugeQueueSw _reader;

	public HugeQueue() {
		_writer = new HugeQueueSw();
		_reader = new HugeQueueSw();
	}

	public void add(String str) {
		_writer.add(str);
	}

	public void add(byte[] block) {
		_writer.add(block);
	}

	private void beforePoll() {
		if(_reader.size() == 0L && _writer.size() != 0L) {
			HugeQueueSw swap = _writer;
			_writer = _reader;
			_reader = swap;
			_writer.reset();
			_reader.switchToRead();
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
		_writer.reset();
		_reader.reset();
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
