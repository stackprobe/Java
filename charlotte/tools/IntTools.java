package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class IntTools {
	public static byte[] toBytes(int value) {
		byte[] ret = new byte[4];
		write(ret, 0, value);
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
}
