package charlotte.tools;

import charlotte.satellite.ObjectMap;

public class WktTools {
	public static String encode(Object src) {
		Encoder e = new Encoder();
		e.add(src);
		return e.get();
	}

	private static class Encoder {
		private StringBuffer _buff = new StringBuffer();

		public void add(Object src) {
			if(src instanceof ObjectMap) {

			}
		}

		public String get() {
			return _buff.toString();
		}
	}
}
