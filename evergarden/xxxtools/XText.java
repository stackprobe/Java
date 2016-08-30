package evergarden.xxxtools;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.StringTools;

public class XText {
	public static class Writer {
		private OutputStreamWriter _w;
		private int _indent = 0;

		public Writer(String file) throws Exception {
			_w = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), StringTools.CHARSET_SJIS);
		}

		public void enter(String name) {
			writeln(name);
			writeln("{");
			_indent++;
		}

		public void leave() {
			_indent--;
			writeln("}");
		}

		public void add(String name, Object value) {
			writeln(name + " = " + value);
		}

		private void writeln(String line) {
			try {
				for(int c = 0; c < _indent; c++) {
					_w.write((int)'\t');
				}
				_w.write(line);
				_w.write((int)'\n');
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}

		public void close() {
			FileTools.close(_w);
		}
	}

	/**
	 * memo -- 読み込み時の責任 @ 2016.3.29
	 * @param src
	 * @return
	 */
	public static String encode(Object src) {
		if(src == null) {
			return "<null>";
		}
		return encode(src.toString());
	}

	public static String encode(String src) {
		if(src == null) {
			return "<null>";
		}
		StringBuffer buff = new StringBuffer();

		for(char chr : src.toCharArray()) {
			if(chr < ' ' || isCharOfSJIS(chr) == false) {
				buff.append("<<0x" + IntTools.toString0x((int)chr, 4) + ">>");
			}
			else {
				buff.append(chr);
			}
		}
		return buff.toString();
	}

	public static boolean isCharOfSJIS(char chr) {
		try {
			return chr == '?' || new String(new char[] { chr }).getBytes(StringTools.CHARSET_SJIS)[0] != 0x3f;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		throw null;
	}
}
