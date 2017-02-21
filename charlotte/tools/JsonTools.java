package charlotte.tools;

public class JsonTools {
	public static String encode(Object src) throws Exception {
		Encoder e = new Encoder();
		e.add(src, "");
		e.addNewLine();
		return e.get();
	}

	private static final String INDENT = "\t";
	private static final String NEW_LINE = "\r\n";

	private static class Encoder {
		private StringBuffer _buff = new StringBuffer();

		public void add(Object src, String indent) {
			if(src instanceof ObjectMap) {
				ObjectMap om = (ObjectMap)src;
				boolean secondOrLater = false;

				_buff.append("{");
				_buff.append(NEW_LINE);

				for(String key : om.keyOrder()) {
					Object value = om.get(key);

					if(secondOrLater) {
						_buff.append(",");
						_buff.append(NEW_LINE);
					}
					_buff.append(indent);
					_buff.append(INDENT);
					_buff.append("\"");
					_buff.append(key);
					_buff.append("\" : ");
					add(value, indent + INDENT);

					secondOrLater = true;
				}
				_buff.append(NEW_LINE);
				_buff.append(indent);
				_buff.append("}");
			}
			else if(src instanceof ObjectList) {
				ObjectList ol = (ObjectList)src;
				boolean secondOrLater = false;

				_buff.append("[");
				_buff.append(NEW_LINE);

				for(Object value : ol.getList()) {
					if(secondOrLater) {
						_buff.append(",");
						_buff.append(NEW_LINE);
					}
					_buff.append(indent);
					_buff.append(INDENT);
					add(value, indent + INDENT);

					secondOrLater = true;
				}
				_buff.append(NEW_LINE);
				_buff.append(indent);
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

		public void addNewLine() {
			_buff.append(NEW_LINE);
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
				return new JsonValue(str);
			}
		}
	}
}
