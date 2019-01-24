package charlotte.tools;

public class JsonTools {
	public static String encode(Object src) throws Exception {
		return encode(src, false);
	}

	public static String encode(Object src, boolean noBlank) throws Exception {
		Encoder e = new Encoder(
				noBlank ? "" : " ",
				noBlank ? "" : "\t",
				noBlank ? "" : "\r\n"
				);
		e.add(src, 0);
		return e.get();
	}

	private static class Encoder {
		private String _blank;
		private String _indent;
		private String _newLine;

		public Encoder(String blank, String indent, String newLine) {
			_blank = blank;
			_indent = indent;
			_newLine = newLine;
		}

		private StringBuffer _buff = new StringBuffer();

		public void add(Object src, int indent) {
			if(src instanceof ObjectMap) {
				ObjectMap om = (ObjectMap)src;
				boolean secondOrLater = false;

				_buff.append("{");
				_buff.append(_newLine);

				for(String key : om.keyOrder()) {
					Object value = om.get(key);

					if(secondOrLater) {
						_buff.append(",");
						_buff.append(_newLine);
					}
					addIndent(indent + 1);
					_buff.append("\"");
					_buff.append(key);
					_buff.append("\"");
					_buff.append(_blank);
					_buff.append(":");
					_buff.append(_blank);
					add(value, indent + 1);

					secondOrLater = true;
				}
				_buff.append(_newLine);
				addIndent(indent);
				_buff.append("}");
			}
			else if(src instanceof ObjectList) {
				ObjectList ol = (ObjectList)src;
				boolean secondOrLater = false;

				_buff.append("[");
				_buff.append(_newLine);

				for(Object value : ol.asList()) {
					if(secondOrLater) {
						_buff.append(",");
						_buff.append(_newLine);
					}
					addIndent(indent + 1);
					add(value, indent + 1);

					secondOrLater = true;
				}
				_buff.append(_newLine);
				addIndent(indent);
				_buff.append("]");
			}
			else if(src instanceof String) {
				String str = (String)src;

				_buff.append("\"");

				for(char chr : str.toCharArray()) {
					if(chr == '"') {
						_buff.append("\\\"");
					}
					else if(chr == '\\') {
						_buff.append("\\\\");
					}
					/*
					else if(chr == '/') {
						_buff.append("\\/");
					}
					*/
					else if(chr == '\b') {
						_buff.append("\\b");
					}
					else if(chr == '\f') {
						_buff.append("\\f");
					}
					else if(chr == '\n') {
						_buff.append("\\n");
					}
					else if(chr == '\r') {
						_buff.append("\\r");
					}
					else if(chr == '\t') {
						_buff.append("\\t");
					}
					else {
						_buff.append(chr);
					}
				}
				_buff.append("\"");
			}
			else {
				_buff.append(src);
			}
		}

		public void addIndent(int count) {
			while(0 < count) {
				_buff.append(_indent);
				count--;
			}
		}

		public String get() {
			return _buff.toString();
		}
	}

	public static Object decode(byte[] src) throws Exception {
		return decode(new String(src, getCharset(src)));
	}

	private static String getCharset(byte[] src) {
		if(4 <= src.length) {
			String x4 = StringTools.toHex(src, 0, 4);

			if("0000feff".equals(x4) || "fffe0000".equals(x4)) {
				return StringTools.CHARSET_UTF32;
			}
		}
		if(2 <= src.length) {
			String x2 = StringTools.toHex(src, 0, 2);

			if("feff".equals(x2) || "fffe".equals(x2)) {
				return StringTools.CHARSET_UTF16;
			}
		}
		return StringTools.CHARSET_UTF8;
	}

	public static Object decode(String src) throws Exception {
		return new Decoder(src).get();
	}

	private static class Decoder {
		private String _src;
		private int _rPos;

		public Decoder(String src) {
			_src = src;
		}

		private char next() {
			return _src.charAt(_rPos++);
		}

		private char nextNS() {
			char chr;

			do {
				chr = next();
			}
			while(chr <= ' ');

			return chr;
		}

		private Object get() throws Exception {
			char chr = nextNS();

			if(chr == '{') {
				ObjectMap om = ObjectMap.createIgnoreCase();

				if(nextNS() != '}') {
					_rPos--;

					do {
						Object key = get();
						nextNS(); // ':'
						Object value = get();
						om.add(key, value);
					}
					while(nextNS() != '}');
				}
				return om;
			}
			if(chr == '[') {
				ObjectList ol = new ObjectList();

				if(nextNS() != ']') {
					_rPos--;

					do {
						ol.add(get());
					}
					while(nextNS() != ']');
				}
				return ol;
			}
			if(chr == '"') {
				StringBuffer buff = new StringBuffer();

				for(; ; ) {
					chr = next();

					if(chr == '"') {
						break;
					}
					if(chr == '\\') {
						chr = next();

						if(chr == 'b') {
							chr = '\b';
						}
						else if(chr == 'f') {
							chr = '\f';
						}
						else if(chr == 'n') {
							chr = '\n';
						}
						else if(chr == 'r') {
							chr = '\r';
						}
						else if(chr == 't') {
							chr = '\t';
						}
						else if(chr == 'u') {
							char c1 = next();
							char c2 = next();
							char c3 = next();
							char c4 = next();

							chr = (char)Integer.parseInt(new String(new char[] { c1, c2, c3, c4 }), 16);
						}
					}
					buff.append(chr);
				}
				return buff.toString();
			}

			{
				StringBuffer buff = new StringBuffer();

				buff.append(chr);

				while(_rPos < _src.length()) {
					chr = next();

					if(chr == '}' ||
							chr == ']' ||
							chr == ',' ||
							chr == ':'
							) {
						_rPos--;
						break;
					}
					buff.append(chr);
				}
				String str = buff.toString();
				str = str.trim();
				return new JsonWord(str);
			}
		}
	}
}
