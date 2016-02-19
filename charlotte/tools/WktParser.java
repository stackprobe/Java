package charlotte.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import charlotte.satellite.ObjectList;

public class WktParser {
	private Stack<ObjectList> _rootStack = new Stack<ObjectList>();
	private ObjectList _root = new ObjectList();
	private String _src;
	private int _rPos;

	private char next() {
		return _src.charAt(_rPos++);
	}

	public void add(String src) throws Exception {
		_src = src;
		_rPos = 0;

		while(_rPos < _src.length()) {
			char chr = next();

			if(chr <= ' ') {
				// noop
			}
			else if(chr == '(') {
				_rootStack.add(_root);
				_root = new ObjectList();
			}
			else if(chr == ')') {
				ObjectList parent = _rootStack.pop();
				parent.add(_root);
				_root = parent;
			}
			else if(chr == ',') {
				_root.add(comma);
			}
			else if(chr == '"') {
				_root.add(readString());
			}
			else {
				_root.add(readWord());
			}
		}
		_src = null;
	}

	private String readString() {
		StringBuffer buff = new StringBuffer();

		for(; ; ) {
			char chr = next();

			if(chr == '"') {
				break;
			}
			if(chr == '\\') {
				chr = next();
			}
			buff.append(chr);
		}
		return buff.toString();
	}

	public static String encodeString(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if(chr == '"' || chr == '\\') {
				buff.append('\\');
			}
			buff.append(chr);
		}
		return buff.toString();
	}

	private String readWord() {
		StringBuffer buff = new StringBuffer();

		_rPos--;

		while(_rPos < _src.length()) {
			char chr = next();

			if(chr <= ' ') {
				break;
			}
			if(chr == '(' ||
					chr == ')' ||
					chr == ','
					) {
				_rPos--;
				break;
			}
			buff.append(chr);
		}
		return buff.toString();
	}

	public static final Comma comma = new Comma();

	public static class Comma {
		@Override
		public String toString() {
			return ",";
		}
	}

	public ObjectList getRoot() {
		return _root;
	}

	public static ObjectList parse(String src) throws Exception {
		WktParser p = new WktParser();
		p.add(src);
		return p.getRoot();
	}

	public static List<ObjectList> findLabelledValue(ObjectList root, String label, boolean recursive) {
		return findLabelledValue(root, label, recursive, new ArrayList<ObjectList>());
	}

	public static List<ObjectList> findLabelledValue(ObjectList root, String label, boolean recursive, List<ObjectList> dest) {
		for(int index = 0; index < root.size(); index++) {
			if(root.get(index) instanceof ObjectList) {
				ObjectList ol = (ObjectList)root.get(index);

				if(1 <= index && root.get(index - 1) instanceof String) {
					String olLabel = (String)root.get(index - 1);

					if(olLabel.equals(label)) {
						dest.add(ol);
					}
				}
				if(recursive) {
					findLabelledValue(ol, label, true, dest);
				}
			}
		}
		return dest;
	}
}
