package charlotte.tools;

public class BinaryTools {
	public static byte[] replace(byte[] src, byte[] from, byte[] to) {
		if(from.length == 0) {
			throw new IllegalArgumentException();
		}
		BlockBuffer dest = new BlockBuffer();
		int wPos = 0;

		for(int index = 0; index + from.length <= src.length; index++) {
			if(ArrayTools.isSame(src, index, from, 0, from.length)) {
				dest.bindAdd(src, wPos, index - wPos);
				dest.bindAdd(to);
				wPos = index + from.length;
			}
		}
		dest.bindAdd(src, wPos);
		return dest.getBytes();
	}
}
