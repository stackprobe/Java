package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFileSorter extends FileSorter<InputStreamReader, OutputStreamWriter, String> {
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
