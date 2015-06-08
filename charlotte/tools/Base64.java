package charlotte.tools;

public class Base64 {
	private static final String CHRS = StringTools.ALPHA + StringTools.alpha + StringTools.DIGIT + "+/";
	private static final StringIndexOf CHRS_I = new StringIndexOf(CHRS);
	private static final char PAD = '=';

	public static String getString(byte[] src) {
		StringBuffer buff = new StringBuffer();
		int index;

		for(index = 0; index + 3 <= src.length; index += 3) {
			int value =
				((src[index + 0] & 0xff) << 16) |
				((src[index + 1] & 0xff) <<  8) |
				((src[index + 2] & 0xff) <<  0);

			buff.append(CHRS.charAt((value >>> 18) & 63));
			buff.append(CHRS.charAt((value >>> 12) & 63));
			buff.append(CHRS.charAt((value >>>  6) & 63));
			buff.append(CHRS.charAt((value >>>  0) & 63));
		}
		int remaining = src.length - index;

		if(remaining == 2) {
			int value =
				((src[index + 0] & 0xff) << 8) |
				((src[index + 1] & 0xff) << 0);

			value = revBit(value, 0, 16);
			value =
				revBit(value,  0, 6) |
				revBit(value,  6, 6) |
				revBit(value, 12, 6);

			buff.append(CHRS.charAt((value >>>  0) & 63));
			buff.append(CHRS.charAt((value >>>  6) & 63));
			buff.append(CHRS.charAt((value >>> 12) & 63));
			buff.append(PAD);
		}
		else if(remaining == 1) {
			int value = src[index] & 0xff;

			value = revBit(value, 0, 8);
			value =
				revBit(value, 0, 6) |
				revBit(value, 6, 6);

			buff.append(CHRS.charAt((value >>>  0) & 63));
			buff.append(CHRS.charAt((value >>>  6) & 63));
			buff.append(PAD);
			buff.append(PAD);
		}
		return buff.toString();
	}

	public static byte[] getBytes(String src) {
		ByteBuffer buff = new ByteBuffer();
		int srcLen = src.length();
		int index;

		while(0 < srcLen && src.charAt(srcLen - 1) == PAD) {
			srcLen--;
		}
		for(index = 0; index + 4 <= srcLen; index += 4) {
			char c1 = src.charAt(index + 0);
			char c2 = src.charAt(index + 1);
			char c3 = src.charAt(index + 2);
			char c4 = src.charAt(index + 3);

			int value =
				(CHRS_I.get(c1) << 18) |
				(CHRS_I.get(c2) << 12) |
				(CHRS_I.get(c3) <<  6) |
				(CHRS_I.get(c4) <<  0);

			buff.add((byte)((value >> 16) & 0xff));
			buff.add((byte)((value >>  8) & 0xff));
			buff.add((byte)((value >>  0) & 0xff));
		}
		int remaining = srcLen - index;

		if(remaining == 3) {
			char c1 = src.charAt(index + 0);
			char c2 = src.charAt(index + 1);
			char c3 = src.charAt(index + 2);

			int value =
				(CHRS_I.get(c1) <<  0) |
				(CHRS_I.get(c2) <<  6) |
				(CHRS_I.get(c3) << 12);

			value =
				revBit(value,  0, 6) |
				revBit(value,  6, 6) |
				revBit(value, 12, 6);
			value =
				revBit(value, 0, 16);

			buff.add((byte)((value >> 8) & 0xff));
			buff.add((byte)((value >> 0) & 0xff));
		}
		else if(remaining == 2) {
			char c1 = src.charAt(index + 0);
			char c2 = src.charAt(index + 1);

			int value =
				(CHRS_I.get(c1) << 0) |
				(CHRS_I.get(c2) << 6);

			value =
				revBit(value,  0, 6) |
				revBit(value,  6, 6);
			value =
				revBit(value, 0, 8);

			buff.add((byte)(value & 0xff));
		}
		return buff.getBytes();
	}

	private static int revBit(int value, int startBit, int bitNum) {
		int l = startBit;
		int h = startBit + bitNum - 1;
		int ret = 0;

		while(l < h) {
			int lb = (value >>> l) & 1;
			int hb = (value >>> h) & 1;

			ret |= lb << h;
			ret |= hb << l;

			l++;
			h--;
		}
		return ret;
	}
}
