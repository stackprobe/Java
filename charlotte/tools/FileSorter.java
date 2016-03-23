package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class FileSorter<Reader, Writer, Record> {
	public void mergeSort(String rwFile) throws Exception {
		mergeSort(rwFile, rwFile);
	}

	private String _divFileBase;
	private long _rSerial;
	private long _wSerial;

	public void mergeSort(String rFile, String wFile) throws Exception {
		_divFileBase = FileTools.makeTempPath() + "_FileSorter_div_";
		_rSerial = 0L;
		_wSerial = 0L;

		try {
			new FileInputStream(rFile).close(); // read check !

			makeDivFiles(rFile);

			while(_rSerial + 2 < _wSerial) {
				String divFile1 = getDivFile(_rSerial++);
				String divFile2 = getDivFile(_rSerial++);
				String divFile3 = getDivFile(_wSerial++);

				mergeFile(divFile1, divFile2, divFile3);
			}

			new FileOutputStream(wFile).close(); // write check !

			switch((int)(_wSerial - _rSerial)) {
			case 2:
				mergeFile(getDivFile(_rSerial++), getDivFile(_rSerial++), wFile);
				break;

			case 1:
				flowFile(getDivFile(_rSerial++), wFile);
				break;

			case 0:
				writeClose(writeOpen(wFile));
				break;

			default:
				throw null;
			}
		}
		finally {
			while(_rSerial < _wSerial) {
				FileTools.del(getDivFile(_rSerial++));
			}
			_divFileBase = null;
			_rSerial = -1L;
			_wSerial = -1L;
		}
	}

	private void makeDivFiles(String rFile) {
		Reader reader = readOpen(rFile);
		List<Record> records = new ArrayList<Record>();
		long weight = 0L;
		long weightMax = getWeightMax();

		for(; ; ) {
			Record record = readRecord(reader);

			if(record == null) {
				break;
			}
			records.add(record);
			weight += getWeight(record);

			if(weightMax < weight) {
				makeDivFile(records);
				records.clear();
				weight = 0L;
			}
		}
		readClose(reader);

		if(1 <= records.size()) {
			makeDivFile(records);
		}
	}

	private void makeDivFile(List<Record> records) {
		String wFile = getDivFile(_wSerial++);

		ArrayTools.sort(records, new Comparator<Record>() {
			@Override
			public int compare(Record a, Record b) {
				return comp(a, b);
			}
		});

		Writer writer = writeOpen(wFile);

		for(Record record : records) {
			writeRecord(writer, record);
		}
		writeClose(writer);
	}

	private String getDivFile(long serial) {
		return _divFileBase + serial;
	}

	private void mergeFile(String rFile1, String rFile2, String wFile) {
		Reader reader1 = readOpen(rFile1);
		Reader reader2 = readOpen(rFile2);
		Writer writer = writeOpen(wFile);
		Record record1 = readRecord(reader1);
		Record record2 = readRecord(reader2);

		for(; ; ) {
			int ret;

			if(record1 == null) {
				if(record2 == null) {
					break;
				}
				ret = 1;
			}
			else if(record2 == null) {
				ret = -1;
			}
			else {
				ret = comp(record1, record2);
			}
			if(ret < 0) {
				writeRecord(writer, record1);
				record1 = readRecord(reader1);
			}
			else if(0 < ret) {
				writeRecord(writer, record2);
				record2 = readRecord(reader2);
			}
			else {
				writeRecord(writer, record1);
				writeRecord(writer, record2);
				record1 = readRecord(reader1);
				record2 = readRecord(reader2);
			}
		}
		readClose(reader1);
		readClose(reader2);
		writeClose(writer);

		FileTools.delete(rFile1);
		FileTools.delete(rFile2);
	}

	private void flowFile(String rFile, String wFile) throws Exception {
		FileTools.copy(rFile, wFile);
		FileTools.delete(rFile);
		// old
		/*
		Reader reader = readOpen(rFile);
		Writer writer = writeOpen(wFile);

		for(; ; ) {
			Record record = readRecord(reader);

			if(record == null) {
				break;
			}
			writeRecord(writer, record);
		}
		readClose(reader);
		writeClose(writer);

		FileTools.delete(rFile);
		*/
	}

	protected abstract Reader readOpen(String file);
	protected abstract Record readRecord(Reader reader);
	protected abstract void readClose(Reader reader);

	protected abstract Writer writeOpen(String file);
	protected abstract void writeRecord(Writer writer, Record record);
	protected abstract void writeClose(Writer writer);

	protected abstract long getWeight(Record record);
	protected abstract long getWeightMax();

	protected abstract int comp(Record a, Record b);

	public static class TextFileSorter extends FileSorter<InputStreamReader, OutputStreamWriter, String> {
		private String _charset;

		public TextFileSorter(String charset) {
			_charset = charset;
		}

		@Override
		protected InputStreamReader readOpen(String file) {
			try {
				return new InputStreamReader(new FileInputStream(file), _charset);
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected String readRecord(InputStreamReader reader) {
			try {
				StringBuffer buff = new StringBuffer();

				for(; ; ) {
					int chr = reader.read();

					if(chr == -1) {
						if(buff.length() == 0) {
							return null;
						}
						break;
					}
					if(chr == '\n') {
						break;
					}
					if(chr == '\r') {
						continue;
					}
					buff.append((char)chr);
				}
				return buff.toString();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void readClose(InputStreamReader reader) {
			FileTools.close(reader);
		}

		@Override
		protected OutputStreamWriter writeOpen(String file) {
			try {
				return new OutputStreamWriter(new FileOutputStream(file), _charset);
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void writeRecord(OutputStreamWriter writer, String record) {
			try {
				writer.write(record);
				writer.write('\r');
				writer.write('\n');
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void writeClose(OutputStreamWriter writer) {
			FileTools.close(writer);
		}

		@Override
		protected long getWeight(String record) {
			return 100 + record.length() * 2;
		}

		@Override
		protected long getWeightMax() {
			return 50000000; // 50 MB
		}

		@Override
		protected int comp(String a, String b) {
			return StringTools.comp.compare(a, b);
		}
	}
}
