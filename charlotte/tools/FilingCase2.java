package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * _rootDir/FilingCase2.sig
 *          tables.dat = num { table-name ... }
 *          tables/ ...
 *
 * ... /[h(table-name)]/table/records/[record-ident]/record.dat = k-num {{ k v } ... }
 *                            uks/ ...
 *
 * ... /[h(column)]/uk/uvs/ ...
 *
 * ... /[h(value)]/uv/record-ident.dat = record-ident
 *
 * [?] = x1/x2/x3/x4/x5/x6/x7/x8/hash-or-ident
 *
 */
public class FilingCase2 {
	private String _rootDir;

	public FilingCase2(String rootDir) throws Exception {
		_rootDir = FileTools.getFullPath(rootDir);
		init();
	}

	private static final int TABLE_MAX = 10000;

	private String _sigFile;
	private String _tablesDir;
	private String _tablesFile;

	private void init() throws Exception {
		_sigFile = FileTools.combine(_rootDir, "FilingCase2.sig");
		_tablesDir = FileTools.combine(_rootDir, "tables");
		_tablesFile = FileTools.combine(_rootDir, "tables.dat");

		if(FileTools.exists(_rootDir) == false) {
			create();
		}
		check();
	}

	private void create() throws Exception {
		FileTools.mkdirs(_rootDir);
		FileTools.createFile(_sigFile);
		FileTools.mkdir(_tablesDir);

		{
			Writer writer = new Writer(_tablesFile);
			try {
				writer.writeInt(0);
			}
			finally {
				FileTools.close(writer);
			}
		}
	}

	private void check() {
		if(FileTools.exists(_sigFile) == false) {
			throw new RuntimeException("Bad _rootDir");
		}
	}

	public List<String> getTables() {
		Reader reader = new Reader(_tablesFile);
		try {
			List<String> tables = new ArrayList<String>();

			for(int count = reader.readInt(); 1 <= count; count--) {
				tables.add(reader.read());
			}
			return tables;
		}
		finally {
			FileTools.close(reader);
		}
	}

	public void add(String table, Map<String, String> record) {
		throw null; // TODO
	}

	public void set(String table, String column, String value, Map<String, String> record) {
		remove(table, column, value);
		add(table, record);
	}

	public void remove(String table, String column, String vlaue) {
		throw null; // TODO
	}

	public void remove(String table, AcceptListener<Map<String, String>> selector) {
		throw null; // TODO
	}

	public void scan(String table, AcceptListener<Map<String, String>> scanner) {
		throw null; // TODO
	}

	public Map<String, String> get(String table, String column, String vlaue) {
		throw null; // TODO
	}

	private boolean isUKColumn(String column) {
		return StringTools.startsWithIgnoreCase(column, "U");
	}

	private String getIdent(String str) {
		try {
			return SecurityTools.getSHA512_128String(str.getBytes(StringTools.CHARSET_UTF8));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getIdent() {
		return SecurityTools.cRandHex();
	}

	private class Reader implements Closeable {
		private HugeQueue.FileReader _reader;

		public Reader(String file) {
			_reader = new HugeQueue.FileReader(file);
		}

		public int readInt() {
			return Integer.parseInt(read());
		}

		public String read() {
			return _reader.pollString();
		}

		@Override
		public void close() throws IOException {
			if(_reader != null) {
				FileTools.close(_reader);
				_reader = null;
			}
		}
	}

	private class Writer implements Closeable {
		private HugeQueue.FileWriter _writer;

		public Writer(String file) {
			_writer = new HugeQueue.FileWriter(file);
		}

		public void writeInt(int value) {
			write("" + value);
		}

		public void write(String str) {
			_writer.add(str);
		}

		@Override
		public void close() throws IOException {
			if(_writer != null) {
				FileTools.close(_writer);
				_writer = null;
			}
		}
	}
}
