package charlotte.tools;

import java.util.List;

public abstract class CsvFileSorter extends FileSorter<CsvData.Stream, CsvData.Stream, List<String>> {
	private String _charset;
	private char _delimiter;

	public CsvFileSorter() {
		this(StringTools.CHARSET_SJIS);
	}

	public CsvFileSorter(String charset) {
		this(charset, ',');
	}

	public CsvFileSorter(String charset, char delimiter) {
		_charset = charset;
		_delimiter = delimiter;
	}

	/*
	public static CsvFileSorter createTsv() {
		return createTsv(StringTools.CHARSET_SJIS);
	}

	public static CsvFileSorter createTsv(String charset) {
		return new CsvFileSorter(charset, '\t');
	}
	*/

	@Override
	protected CsvData.Stream readOpen(String file) {
		try {
			CsvData.Stream reader = new CsvData.Stream(file, _charset, _delimiter);
			reader.readOpen();
			return reader;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected List<String> readRecord(CsvData.Stream reader) {
		try {
			return reader.readRow();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void readClose(CsvData.Stream reader) {
		reader.readClose();
	}

	@Override
	protected CsvData.Stream writeOpen(String file) {
		try {
			CsvData.Stream writer = new CsvData.Stream(file, _charset, _delimiter);
			writer.writeOpen();
			return writer;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void writeRecord(CsvData.Stream writer, List<String> record) {
		try {
			writer.writeRow(record);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void writeClose(CsvData.Stream writer) {
		writer.writeClose();
	}

	@Override
	protected long getWeight(List<String> record) {
		long weight = 100;

		for(String cell : record) {
			weight += 100 + cell.length() * 2;
		}
		return weight;
	}

	@Override
	protected long getWeightMax() {
		return 50000000; // 50 MB
	}

	protected static int compSample(List<String> a, List<String> b) {
		return comp(a, b, 0, 1, 2);
	}

	protected static int comp(List<String> a, List<String> b, int ... indexes) {
		for(int index : indexes) {
			int ret = compIndex(a, b, index);

			if(ret != 0) {
				return ret;
			}
		}
		return 0;
	}

	/**
	 * column is nothing < any column
	 *
	 * @param a
	 * @param b
	 * @param index
	 * @return
	 */
	protected static int compIndex(List<String> a, List<String> b, int index) {
		if(a.size() <= index) {
			if(b.size() <= index) {
				return 0;
			}
			return -1;
		}
		else if(b.size() <= index) {
			return 1;
		}
		return comp(a.get(index), b.get(index));
	}

	protected static int comp(String a, String b) {
		try {
			return LongTools.comp.compare(Long.parseLong(a), Long.parseLong(b));
		}
		catch(Throwable e) {
			// ignore
		}

		try {
			return DoubleTools.comp.compare(Double.parseDouble(a), Double.parseDouble(b));
		}
		catch(Throwable e) {
			// ignore
		}

		return StringTools.comp.compare(a, b);
	}
}
