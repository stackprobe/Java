package charlotte.tools;

import charlotte.satellite.ObjectList;
import charlotte.satellite.ObjectMap;

public class JsonTools {
	public static String encode(Object src) throws Exception {
		Encoder e = new Encoder();
		e.add(src);
		return e.get();
	}

	private static class Encoder {
		private StringBuffer _buff = new StringBuffer();

		public void add(Object src) {
			if(src instanceof ObjectMap) {
				ObjectMap om = (ObjectMap)src;
				boolean secondOrLater = false;

				_buff.append("{");

				for(String key : om.keySet()) {
					Object value = om.get(key);

					if(secondOrLater) {
						_buff.append(",");
					}
					_buff.append(key);
					_buff.append(":");
					add(value);

					secondOrLater = true;
				}
				_buff.append("}");
			}
			else if(src instanceof ObjectList) {
				ObjectList ol = (ObjectList)src;
				boolean secondOrLater = false;

				_buff.append("[");

				for(Object value : ol.getList()) {
					if(secondOrLater) {
						_buff.append(",");
					}
					add(value);

					secondOrLater = true;
				}
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
					else if(chr == '/') {
						_buff.append("\\/");
					}
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

		public String get() {
			return _buff.toString();
		}
	}

	public static Object decode(byte[] src) throws Exception {
		return decode(new String(src, StringTools.CHARSET_UTF8)); // TODO compatible other encodings
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

				do {
					Object key = get();
					nextNS(); // ':'
					Object value = get();
					om.add(key, value);
				}
				while(nextNS() != '}');

				return om;
			}
			if(chr == '[') {
				ObjectList ol = new ObjectList();

				do {
					ol.add(get());
				}
				while(nextNS() != ']');

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
							chr == ','
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
