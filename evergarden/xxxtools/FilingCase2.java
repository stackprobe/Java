package evergarden.xxxtools;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import charlotte.tools.AcceptListener;
import charlotte.tools.FileTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.SecurityTools;
import charlotte.tools.SetTools;
import charlotte.tools.StringTools;

/**
 * _rootDir/FilingCase2.sig
 *          tables.dat = { table-name ... }
 *          tables/ ...
 *
 * ... /[h(table-name)]/table/records/[record-ident]/record.dat = {{ k v } ... }
 *                            rks/ ...
 *
 * ... /[h(column)]/rk/rvs/ ...
 *
 * ... /[h(value)]/rv/record-ident.dat = { record-ident ... }
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
		FileTools.createFile(_tablesFile);
	}

	private void check() {
		if(FileTools.exists(_sigFile) == false) {
			throw new IllegalArgumentException();
		}
	}

	public List<String> getTables() {
		Reader reader = new Reader(_tablesFile);
		try {
			List<String> tables = new ArrayList<String>();

			for(; ; ) {
				String table = reader.read();

				if(table == null) {
					break;
				}
				tables.add(table);
			}
			return tables;
		}
		finally {
			FileTools.close(reader);
		}
	}

	public void add(String table, Map<String, String> record) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(isFairRecord(record) == false) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	public boolean remove(String table, String column, String vlaue) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(column == null) {
			throw new IllegalArgumentException();
		}
		if(isRKColumn(column) == false) {
			throw new IllegalArgumentException();
		}
		if(vlaue == null) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	public void remove(String table, AcceptListener<Map<String, String>> selector) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(selector == null) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	public void scan(String table, AcceptListener<Map<String, String>> scanner) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(scanner == null) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	public void scan(String table, String column, String vlaue, AcceptListener<Map<String, String>> scanner) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(column == null) {
			throw new IllegalArgumentException();
		}
		if(isRKColumn(column) == false) {
			throw new IllegalArgumentException();
		}
		if(scanner == null) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	public Map<String, String> get(String table, String column, String vlaue) {
		if(table == null) {
			throw new IllegalArgumentException();
		}
		if(column == null) {
			throw new IllegalArgumentException();
		}
		if(isRKColumn(column) == false) {
			throw new IllegalArgumentException();
		}
		if(vlaue == null) {
			throw new IllegalArgumentException();
		}
		throw null; // TODO
	}

	private boolean isFairRecord(Map<String, String> record) {
		if(record == null) {
			return false;
		}
		Set<String> columns = record.keySet();

		if(columns == null) {
			return false;
		}
		Set<String> knownColumns = SetTools.create();

		for(String column : columns) {
			if(column == null) {
				return false;
			}
			String value = record.get(column);

			if(value == null) {
				return false;
			}
			if(knownColumns.contains(column)) {
				return false;
			}
			knownColumns.add(column);
		}
		return true;
	}

	private boolean isRKColumn(String column) {
		return StringTools.startsWithIgnoreCase(column, "R");
	}

	private String getIdent(String str) {
		return getHash(str);
	}

	private String getIdent() {
		return SecurityTools.cRandHex();
	}

	private String getHash(String str) {
		try {
			return SecurityTools.getSHA512_128String(str.getBytes(StringTools.CHARSET_UTF8));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private class Reader implements Closeable {
		private HugeQueue.FileReader _reader;

		public Reader(String file) {
			_reader = new HugeQueue.FileReader(file);
		}

		public String read() {
			try {
				return _reader.pollString();
			}
			catch(Throwable e) {
				// ignore
			}
			return null;
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
