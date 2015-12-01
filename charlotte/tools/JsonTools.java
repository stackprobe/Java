package charlotte.tools;

import charlotte.satellite.ObjectList;
import charlotte.satellite.ObjectMap;

public class JsonTools {
	public static String encode(Object src) throws Exception {
		if(src instanceof ObjectMap) {
			ObjectMap om = (ObjectMap)src;
			boolean secondOrLater = false;
			StringBuffer buff = new StringBuffer();

			buff.append("{");

			for(String key : om.keySet()) {
				Object value = om.get(key);

				if(secondOrLater) {
					buff.append(",");
				}
				buff.append(key);
				buff.append(":");
				buff.append(encode(value));

				secondOrLater = true;
			}
			buff.append("}");

			return buff.toString();
		}
		if(src instanceof ObjectList) {
			ObjectList ol = (ObjectList)src;
			boolean secondOrLater = false;
			StringBuffer buff = new StringBuffer();

			buff.append("[");

			for(Object value : ol.getList()) {
				if(secondOrLater) {
					buff.append(",");
				}
				buff.append(encode(value));

				secondOrLater = true;
			}
			buff.append("]");

			return buff.toString();
		}
		if(src instanceof String) {
			String str = (String)src;
			StringBuffer buff = new StringBuffer();

			buff.append("\"");

			for(char chr : str.toCharArray()) {
				if(chr == '"') {
					buff.append("\\\"");
				}
				else if(chr == '\\') {
					buff.append("\\\\");
				}
				else if(chr == '/') {
					buff.append("\\/");
				}
				else if(chr == '\b') {
					buff.append("\\b");
				}
				else if(chr == '\f') {
					buff.append("\\f");
				}
				else if(chr == '\n') {
					buff.append("\\n");
				}
				else if(chr == '\r') {
					buff.append("\\r");
				}
				else if(chr == '\t') {
					buff.append("\\t");
				}
				else {
					buff.append(chr);
				}
			}
			buff.append("\"");

			return buff.toString();
		}
		return src.toString();
	}

	public static Object decode(String src) throws Exception {
		return new Decoder(src).decode();
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

		private Object decode() throws Exception {
			char chr = nextNS();

			if(chr == '{') {
				ObjectMap om = new ObjectMap();

				do {
					Object key = decode();
					nextNS(); // ':'
					Object value = decode();
					om.add(key, value);
				}
				while(nextNS() == '}');

				return om;
			}
			if(chr == '[') {
				ObjectList ol = new ObjectList();

				do {
					ol.add(decode());
				}
				while(nextNS() == ']');

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
