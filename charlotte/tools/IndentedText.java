package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class IndentedText {
	private String _indent = "\t";
	private int _deep = 0;

	public IndentedText() {
	}

	public IndentedText(String indent) {
		_indent = indent;
	}

	public IndentedText(String indent, int deep) {
		_indent = indent;
		_deep = deep;
	}

	private List<String> _lines = new ArrayList<String>();

	public void add(List<String> lines) {
		add(lines.toArray(new String[lines.size()]));
	}

	public void add(String[] lines) {
		for(String line : lines) {
			add(line);
		}
	}

	public void add(String line) {
		StringBuffer buff = new StringBuffer();

		for(int c = 0; c < _deep; c++) {
			buff.append(_indent);
		}
		buff.append(line);

		_lines.add(buff.toString());
	}

	public void down() {
		_deep++;
	}

	public void up() {
		_deep--;
	}

	public List<String> getLines() {
		return _lines;
	}

	public static class WktBuffer {
		private IndentedText _it = new IndentedText("  ");
		private boolean _added = false;

		public void enter(String title) {
			putComma();
			_it.add(title + "(");
			_it.down();
			_added = false;
		}

		private void putComma() {
			if(_added) {
				List<String> lines = _it.getLines();
				int index = lines.size() - 1;

				lines.set(index, lines.get(index) + ",");
			}
		}

		public void add(String line) {
			putComma();
			_it.add(line);
			_added = true;
		}

		public void addWkt(List<String> wkt) {
			addWkt(wkt.toArray(new String[wkt.size()]));
		}

		public void addWkt(String[] wkt) {
			putComma();
			_it.add(wkt);
			_added = true;
		}

		public void leave() {
			_it.up();
			_it.add(")");
			_added = true;
		}

		public String getWkt() {
			return StringTools.join("\r\n", _it.getLines());
		}
	}
}
