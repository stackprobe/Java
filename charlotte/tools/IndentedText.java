package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class IndentedText {
	private List<String> _lines = new ArrayList<String>();
	private String _indent = "\t";
	private int _deep = 0;

	public IndentedText() {
	}

	public IndentedText(String indent) {
		_indent = indent;
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
			addComma();
			_it.add(title + "(");
			_it.down();
			_added = false;
		}

		private void addComma() {
			if(_added) {
				List<String> lines = _it.getLines();
				int index = lines.size() - 1;

				lines.set(index, lines.get(index) + ",");
			}
		}

		public void add(String line) {
			addComma();
			_it.add(line);
			_added = true;
		}

		public void leave() {
			_it.up();
			_it.add(")");
			_added = true;
		}

		public String getText() {
			return StringTools.join("\r\n", _it.getLines());
		}
	}
}
