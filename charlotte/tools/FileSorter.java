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

	public void mergeSort(String rFile, String wFile) throws Exception {
		new FileInputStream(rFile).close(); // read check !

		QueueData<String> divFiles = makeDivFiles(rFile);

		while(2 < divFiles.size()) {
			String divFile1 = divFiles.poll();
			String divFile2 = divFiles.poll();
			String divFile3 = FileTools.makeTempPath();

			mergeFile(divFile1, divFile2, divFile3);

			divFiles.add(divFile3);
		}

		new FileOutputStream(wFile).close(); // write check !

		switch(divFiles.size()) {
		case 2:
			mergeFile(divFiles.poll(), divFiles.poll(), wFile);
			break;

		case 1:
			flowFile(divFiles.poll(), wFile);
			break;

		case 0:
			writeClose(writeOpen(wFile));
			break;

		default:
			throw null;
		}
	}

	private QueueData<String> makeDivFiles(String rFile) {
		Reader reader = readOpen(rFile);
		List<Record> records = new ArrayList<Record>();
		long weight = 0L;
		long weightMax = getWeightMax();
		QueueData<String> divFiles = new QueueData<String>();

		for(; ; ) {
			Record record = readRecord(reader);

			if(record == null) {
				break;
			}
			records.add(record);
			weight += getWeight(record);

			if(weightMax < weight) {
				divFiles.add(makeDivFile(records));
				records.clear();
				weight = 0L;
			}
		}
		readClose(reader);

		if(1 <= records.size()) {
			divFiles.add(makeDivFile(records));
		}
		return divFiles;
	}

	private String makeDivFile(List<Record> records) {
		String wFile = FileTools.makeTempPath();

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
		return wFile;
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
