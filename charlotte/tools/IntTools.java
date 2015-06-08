package charlotte.tools;


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
}
