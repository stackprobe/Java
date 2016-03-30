package charlotte.tools;

import java.util.Comparator;

public class LongTools {
	public static long divRndOff(long numer, long denom) {
		return divRndOff(numer, denom, 1, 2);
	}

	public static long divRndOff(long numer, long denom, long rndOffRateNumer, long rndOffRateDenom) {
		return numer / denom + (((numer % denom) * rndOffRateDenom) / rndOffRateNumer) / denom;
	}

	public static final long UINT_MAX = 0xffffffffL;

	public static boolean isRange(long value, long minval, long maxval) {
		return minval <= value && value <= maxval;
	}

	public static Comparator<Long> comp = new Comparator<Long>() {
		@Override
		public int compare(Long v1, Long v2) {
			if(v1 < v2) {
				return 1;
			}
			if(v2 < v1) {
				return -1;
			}
			return 0;
		}
	};

	public static String toString0x(int value) {
		return toString0x(value, 8);
	}

	public static String toString0x(int value, int size) {
		StringBuffer buff = new StringBuffer();

		for(int c = 0; c < size; c++) {
			buff.append(StringTools.hexadecimal.charAt((int)(value & 0xfL)));
			value >>>= 4;
		}
		buff.reverse();
		return buff.toString();
	}
}
