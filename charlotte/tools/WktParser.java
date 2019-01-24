package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class WktParser {
	public static final int KIND_BLANK = 1;
	public static final int KIND_L = 2;
	public static final int KIND_R = 3;
	public static final int KIND_COMMA = 4;
	public static final int KIND_STRING = 5;
	public static final int KIND_WORD = 6;

	public class Token {
		public int kind;
		public String value;

		public Token(int kind, String value) {
			this.kind = kind;
			this.value = value;
		}

		public String getWkt() {
			if(kind == KIND_STRING) {
				return "\"" + encodeString(value) + "\"";
			}
			return value;
		}
	}

	private List<Token> _tokens = new ArrayList<Token>();
	private String _src;
	private int _rPos;

	private boolean hasNext() {
		return _rPos < _src.length();
	}

	private char next() {
		return _src.charAt(_rPos++);
	}

	private void back() {
		_rPos--;
	}

	public void add(String src) {
		_src = src;
		_rPos = 0;

		while(hasNext()) {
			char chr = next();

			if(chr <= ' ') {
				back();
				_tokens.add(new Token(KIND_BLANK, readBlank()));
			}
			else if(chr == '(') {
				_tokens.add(new Token(KIND_L, "("));
			}
			else if(chr == ')') {
				_tokens.add(new Token(KIND_R, ")"));
			}
			else if(chr == ',') {
				_tokens.add(new Token(KIND_COMMA, ","));
			}
			else if(chr == '"') {
				_tokens.add(new Token(KIND_STRING, readString()));
			}
			else {
				back();
				_tokens.add(new Token(KIND_WORD, readWord()));
			}
		}
		_src = null;
		_rPos = -1;
	}

	private String readBlank() {
		StringBuffer buff = new StringBuffer();

		while(hasNext()) {
			char chr = next();

			if(' ' < chr) {
				back();
				break;
			}
			buff.append(chr);
		}
		return buff.toString();
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

		while(hasNext()) {
			char chr = next();

			if(chr <= ' ' ||
					chr == '(' ||
					chr == ')' ||
					chr == ',' ||
					chr == '"'
					) {
				back();
				break;
			}
			buff.append(chr);
		}
		return buff.toString();
	}

	public String getWkt() {
		StringBuffer buff = new StringBuffer();

		for(Token token : _tokens) {
			buff.append(token.getWkt());
		}
		return buff.toString();
	}

	public SubEntities getEntities() {
		return new SubEntities(new Entities());
	}

	public class Entities {
		private List<Token> _entities;

		public Entities() {
			_entities = new ArrayList<Token>();

			for(Token token : _tokens) {
				if(token.kind != KIND_BLANK) {
					_entities.add(token);
				}
			}
		}

		public int size() {
			return _entities.size();
		}

		public Token get(int index) {
			return _entities.get(index);
		}
	}

	public class SubEntities {
		private Entities _entities;
		private int _start;
		private int _count;

		public SubEntities(Entities entities) {
			this(entities, 0, entities.size());
		}

		public SubEntities(Entities entities, int start, int count) {
			_entities = entities;
			_start = start;
			_count = count;
		}

		public int size() {
			return _count;
		}

		public Token get(int index) {
			if(index < 0 || _count <= index) {
				return null;
			}
			return _entities.get(_start + index);
		}

		public int getLabel(String label) {
			return getLabel(label, 0);
		}

		public int getLabel(String label, int fromIndex) {
			for(int index = fromIndex; index + 1 < size(); index++) {
				if(get(index).kind == KIND_WORD &&
						get(index).value.equals(label) &&
						get(index + 1).kind == KIND_L
						) {
					return index;
				}
			}
			return -1; // not found
		}

		private int getR(int fromIndex) {
			int deep = 1;

			for(int index = fromIndex; index < size(); index++) {
				if(get(index).kind == KIND_L) {
					deep++;
				}
				else if(get(index).kind == KIND_R) {
					deep--;

					if(deep == 0) {
						return index;
					}
				}
			}
			return -1; // not found
		}

		public List<SubEntities> getBlocks(String label) {
			List<SubEntities> dest = new ArrayList<SubEntities>();

			for(int index = 0; ; ) {
				index = getLabel(label, index);

				if(index == -1) {
					break;
				}
				index += 2;
				int end = getR(index);

				if(end == -1) {
					throw new RuntimeException("wkt label '" + label + "' not closed");
					//break;
				}
				dest.add(new SubEntities(_entities, _start + index, end - index));
				index = end + 1;
			}
			return dest;
		}

		public SubEntities getBlock(String label) {
			List<SubEntities> ret = getBlocks(label);

			if(ret.size() == 0) {
				throw new RuntimeException("wkt label '" + label + "' does not exist");
				//return null;
			}
			return ret.get(0);
		}
	}
}
