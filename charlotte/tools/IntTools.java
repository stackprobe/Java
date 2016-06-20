package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class IntTools {
	public static final int IMAX = 1000000000;

	public static byte[] toBytes(int value) {
		byte[] ret = new byte[4];
		write(ret, 0, value);
		return ret;
	}

	public static byte[] toBytesBE(int value) {
		byte[] ret = new byte[4];
		write(ret, 0, value);
		ArrayTools.reverse(ret);
		return ret;
	}


	/**
	 * little endian
	 * @param block
	 * @param wPos
	 * @param value
	 */
	public static void write(byte[] block, int wPos, int value) {
		block[wPos + 0] = (byte)((value >>>  0) & 0xff);
		block[wPos + 1] = (byte)((value >>>  8) & 0xff);
		block[wPos + 2] = (byte)((value >>> 16) & 0xff);
		block[wPos + 3] = (byte)((value >>> 24) & 0xff);
	}

	public static int read(byte[] block) {
		return read(block, 0);
	}

	/**
	 * little endian
	 * @param block
	 * @param rPos
	 * @return
	 */
	public static int read(byte[] block, int rPos) {
		return
				((block[rPos + 0] & 0xff) <<  0) |
				((block[rPos + 1] & 0xff) <<  8) |
				((block[rPos + 2] & 0xff) << 16) |
				((block[rPos + 3] & 0xff) << 24);
	}

	public static int pow(int value, int exponent) {
		if(exponent < 0) {
			return 0;
		}
		if(exponent == 0) {
			return 1;
		}
		if(exponent == 1) {
			return value;
		}
		int ret = pow(value, exponent / 2);
		ret *= ret;
		ret *= pow(value, exponent % 2);
		return ret;
	}

	public static Integer[] toIntegers(int[] src) {
		Integer[] dest = new Integer[src.length];

		for(int index = 0; index < src.length; index++) {
			dest[index] = Integer.valueOf(src[index]);
		}
		return dest;
	}

	public static int[] toInts(List<Integer> src) {
		return toInts(src.toArray(new Integer[src.size()]));
	}

	public static int[] toInts(Integer[] src) {
		int[] dest = new int[src.length];
		toInts(src, dest);
		return dest;
	}

	public static void toInts(Integer[] src, int[] dest) {
		for(int index = 0; index < src.length; index++) {
			dest[index] = src[index].intValue();
		}
	}

	public static void sort(int[] array, Comparator<Integer> comp) {
		Integer[] integers = toIntegers(array);
		ArrayTools.sort(integers, comp);
		toInts(integers, array);
	}

	public static int[] getSorted(int[] array, Comparator<Integer> comp) {
		Integer[] integers = toIntegers(array);
		ArrayTools.sort(integers, comp);
		return toInts(integers);
	}

	public static int toInt(double value) {
		if(value < 0.0) {
			return (int)(value - 0.5);
		}
		return (int)(value + 0.5);
	}

	public static int toInt(String str, int defval) {
		try {
			return Integer.parseInt(str, defval);
		}
		catch(Throwable e) {
			// ignore
		}
		return defval;
	}

	public static int toInt(String str, int minval, int maxval, int defval) {
		try {
			int value = Integer.parseInt(str);

			if(isRange(value, minval, maxval) == false) {
				return defval;
			}
			return value;
		}
		catch(Throwable e) {
			// ignore
		}
		return defval;
	}

	public static boolean isRange(int value, int minval, int maxval) {
		return minval <= value && value <= maxval;
	}

	public static int toRange(int value, int minval, int maxval) {
		value = Math.max(value, minval);
		value = Math.min(value, maxval);
		return value;
	}

	public static int toUnsignedInt(String str, int defval) {
		return toUnsignedInt(str, 10, defval);
	}

	public static int toUnsignedInt(String str, int radix, int defval) {
		try {
			return (int)(Long.parseLong(str, radix) & 0xffffffffL);
		}
		catch(Throwable e) {
			// ignore
		}
		return defval;
	}

	public static Comparator<Integer> comp = new Comparator<Integer>() {
		@Override
		public int compare(Integer a, Integer b) {
			int v1 = a.intValue();
			int v2 = b.intValue();

			if(v1 < v2) {
				return -1;
			}
			if(v2 < v1) {
				return 1;
			}
			return 0;
		}
	};

	public static int toInt(byte[] block, int rPos) {
		byte b1 = block[rPos + 0];
		byte b2 = block[rPos + 1];
		byte b3 = block[rPos + 2];
		byte b4 = block[rPos + 3];

		return ((b1 & 0xff) << 0) |
				((b2 & 0xff) << 8) |
				((b3 & 0xff) << 16) |
				((b4 & 0xff) << 24);
	}

	public static int toInt16(byte[] block, int rPos) {
		byte b1 = block[rPos + 0];
		byte b2 = block[rPos + 1];

		return (b1 & 0xff) | ((b2 & 0xff) << 8);
	}

	public static String toString0x(int value) {
		return toString0x(value, 8);
	}

	public static String toString0x(int value, int size) {
		StringBuffer buff = new StringBuffer();

		for(int c = 0; c < size; c++) {
			buff.append(StringTools.hexadecimal.charAt(value & 0xf));
			value >>>= 4;
		}
		buff.reverse();
		return buff.toString();
	}

	public static int revEndian(int value) {
		return ((value >>> 24) & 0x000000ff) |
				((value >>> 8) & 0x0000ff00) |
				((value <<  8) & 0x00ff0000) |
				((value << 24) & 0xff000000);
	}

	public static int toInt(String str, String digits) {
		int radix = digits.length();
		int ret = 0;

		str = str.toLowerCase();

		for(char chr : str.toCharArray()) {
			if(chr == '-') {
				ret *= -1;
			}
			else {
				int val = digits.indexOf(chr);

				if(val != -1) {
					ret *= radix;
					ret += val;
				}
			}
		}
		return ret;
	}

	public static int hex(String str) {
		return toInt(str, StringTools.hexadecimal);
	}

	public static int oct(String str) {
		return toInt(str, StringTools.octodecimal);
	}

	public static int bin(String str) {
		return toInt(str, StringTools.BINADECIMAL);
	}

	public static String toString(int value, int radix) {
		return Integer.toString(value, radix);
	}

	public static String toHex(int value) {
		return toString(value, 16);
	}

	public static String toOct(int value) {
		return toString(value, 8);
	}

	public static String toBin(int value) {
		return toString(value, 2);
	}
}
