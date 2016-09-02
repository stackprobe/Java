package charlotte.tools;

import java.util.Map;
import java.util.Set;

public class PropertiesData {
	public static PropertiesData create() {
		return new PropertiesData(MapTools.<String>create());
	}

	public static PropertiesData createIgnoreCase() {
		return new PropertiesData(MapTools.<String>createIgnoreCase());
	}

	private Map<String, String> _values;

	public PropertiesData(Map<String, String> bind_values) {
		_values = bind_values;
	}

	public Set<String> keySet() {
		return _values.keySet();
	}

	public String get(String key) {
		return _values.get(key);
	}

	public void add(String key, String value) {
		_values.put(key, value);
	}

	public void add(Map<String, String> values) {
		_values.putAll(values);
	}

	public void addFile(String file) throws Exception {
		addFile(file, StringTools.CHARSET_UTF8);
	}

	public void addFile(String file, String charset) throws Exception {
		for(String line : FileTools.readAllLines(file, charset)) {
			line = parseLine(line);
			line = line.trim();

			if(line.startsWith("#") == false) {
				int p = line.indexOf('=');

				if(p != -1) {
					String key = line.substring(0, p);
					String value = line.substring(p + 1);

					key = key.trim();
					value = value.trim();

					add(key, value);
				}
			}
		}
	}

	private static String parseLine(String line) throws Exception {
		StringBuffer buff = new StringBuffer();
		ByteBuffer q = null;

		for(int index = 0; index < line.length(); index++) {
			if(index + 6 <= line.length() && isEscapedUnicode(line.substring(index, index + 6))) {
				if(q == null) {
					q = new ByteBuffer();
				}
				q.bindAdd(StringTools.hex(line.substring(index + 2, index + 6)));
				index += 5;
			}
			else {
				if(q != null) {
					buff.append(toString_escapedUnicode(q));
					q = null;
				}
				buff.append(line.charAt(index));
			}
		}
		if(q != null) {
			buff.append(toString_escapedUnicode(q));
		}
		return buff.toString();
	}

	private static boolean isEscapedUnicode(String str) {
		str = str.toLowerCase();
		str = StringTools.replaceChar(str, StringTools.hexadecimal, '9');

		return "\\u9999".equals(str);
	}

	private static String toString_escapedUnicode(ByteBuffer buff) throws Exception {
		return new String(buff.getBytes(), StringTools.CHARSET_UTF16BE);
	}
}
