package evergarden.xxxtools;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.StringTools;

public class HugeFileQueue implements Closeable {
	private String _file;
	private FileOutputStream _writer;
	private FileInputStream _reader;
	private long _size = 0L;

	public HugeFileQueue() {
		try {
			_file = FileTools.makeTempPath();
			_writer = new FileOutputStream(_file);
			_reader = new FileInputStream(_file);
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
			_size++;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] poll() {
		if(_size == 0L) {
			return null;
		}
		_size--;

		byte[] block = readBlock(
				IntTools.toInt(readBlock(4), 0)
				);

		if(_size == 0L) {
			clear();
		}
		return block;
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

	public void clear() {
		try {
			FileTools.close(_writer);
			FileTools.close(_reader);
			_writer = new FileOutputStream(_file);
			_reader = new FileInputStream(_file);
			_size = 0L;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
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
