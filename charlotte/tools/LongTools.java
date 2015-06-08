package charlotte.tools;

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
}
