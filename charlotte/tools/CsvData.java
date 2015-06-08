package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class CsvData {
	private char _delimiter;
	private List<List<String>> _rows = new ArrayList<List<String>>();

	public CsvData() {
		this(',');
	}

	public CsvData(char delimiter) {
		_delimiter = delimiter;
	}

	public void clear() {
		_rows.clear();
	}

	public void readFile(String csvFile) throws Exception {
		this.readFile(csvFile, StringTools.CHARSET_SJIS);
	}

	public void readFile(String csvFile, String charset) throws Exception {
		this.readText(new String(FileTools.readAllBytes(csvFile), charset));
	}

	private String _text;
	private int _rPos;

	private int nextChar() {
		char chr;

		do {
			if(_text.length() <= _rPos) {
				return -1;
			}
			chr = _text.charAt(_rPos);
			_rPos++;
		}
		while(chr == '\r');

		return chr;
	}

	public void readText(String text) {
		_text = text;
		_rPos = 0;

		List<String> row = new ArrayList<String>();

		this.clear();

		for(; ; ) {
			int chr = nextChar();

			if(chr == -1) {
				break;
			}
			StringBuffer buff = new StringBuffer();

			if(chr == '"') {
				for(; ; ) {
					chr = nextChar();

					if(chr == -1) {
						break;
					}
					if(chr == '"') {
						chr = nextChar();

						if(chr != '"') {
							break;
						}
					}
					buff.append((char)chr);
				}
			}
			else {
				for(; ; ) {
					if(chr == _delimiter || chr == '\n') {
						break;
					}
					buff.append((char)chr);
					chr = nextChar();

					if(chr == -1) {
						break;
					}
				}
			}
			row.add(buff.toString());

			if(chr == '\n') {
				_rows.add(row);
				row = new ArrayList<String>();
			}
		}
		if(1 <= row.size()) {
			_rows.add(row);
		}
		_text = null;
	}

	public String getText() {
		StringBuffer buff = new StringBuffer();

		for(List<String> row : _rows) {
			for(int colidx = 0; colidx < row.size(); colidx++) {
				if(1 <= colidx) {
					buff.append(_delimiter);
				}
				String cell = row.get(colidx);

				if(StringTools.containsChar(cell, "\r\n\"" + _delimiter)) {
					String dq2dqdqCell = cell.replace("\"", "\"\"");

					buff.append('"');
					buff.append(dq2dqdqCell);
					buff.append('"');
				}
				else {
					buff.append(cell);
				}
			}
			buff.append('\n');
		}
		return buff.toString();
	}

	public void writeFile(String csvFile) throws Exception {
		this.writeFile(csvFile, StringTools.CHARSET_SJIS);
	}

	public void writeFile(String csvFile, String charset) throws Exception {
		FileTools.writeAllBytes(csvFile, getText().getBytes(charset));
	}

	public void tt() {
		this.trimAllCell();
		this.trim();
	}

	public void ttr() {
		this.trimAllCell();
		this.trim();
		this.toRect();
	}

	public void trimAllCell() {
		for(List<String> row : _rows) {
			for(int colidx = 0; colidx < row.size(); colidx++) {
				String cell = row.get(colidx);
				cell = cell.trim();
				row.set(colidx, cell);
			}
		}
	}

	public void trim() {
		for(List<String> row : _rows) {
			while(1 <= row.size() && StringTools.isEmpty(row.get(row.size() - 1))) {
				row.remove(row.size() - 1);
			}
		}
		while(1 <= _rows.size() && _rows.get(_rows.size() - 1).size() == 0) {
			_rows.remove(_rows.size() - 1);
		}
	}

	public void toRect() {
		int w = 0;

		for(List<String> row : _rows) {
			w = Math.max(w, row.size());
		}
		for(List<String> row : _rows) {
			while(row.size() < w) {
				row.add("");
			}
		}
	}
}
