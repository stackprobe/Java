package charlotte.tools;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class SecurityTools {
	public static String getSHA512_128String(String str) throws Exception {
		/*
		String ret = getSHA512String(str).substring(0, 32);
		System.out.println(str + " -> " + ret);
		return ret;
		*/
		return getSHA512String(str).substring(0, 32);
	}

	public static String getSHA512String(String str) throws Exception {
		return getSHA512String(str, StringTools.CHARSET_SJIS);
	}

	public static String getSHA512String(String str, String charset) throws Exception {
		return getSHA512String(str.getBytes(charset));
	}

	public static String getSHA512_128String(byte[] block) throws Exception {
		return getSHA512String(block).substring(0, 32);
	}

	public static String getSHA512String(byte[] block) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(block);
		byte[] digest = md.digest();
		return StringTools.toHex(digest);
	}

	public static byte[] getSHA512(String str) throws Exception {
		return getSHA512(str, StringTools.CHARSET_SJIS);
	}

	public static byte[] getSHA512(String str, String charset) throws Exception {
		return getSHA512(str.getBytes(charset));
	}

	public static byte[] getSHA512(byte[] block) throws Exception {
		return getSHA512(new byte[][] { block });
	}

	public static byte[] getSHA512(byte[][] blocks) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-512");

		for(byte[] block : blocks) {
			md.update(block);
		}
		byte[] ret = md.digest();
		return ret;
	}

	private static Random _random = new SecureRandom();

	public static int random(int modulo) {
		return _random.nextInt(modulo);
	}

	public static byte[] randSq(byte[] dest, int startPos, int size) {
		for(int index = 0; index < size; index++) {
			dest[startPos + index] = (byte)_random.nextInt(256);
		}
		return dest;
	}

	public static byte[] randSq(int size) {
		return randSq(new byte[size], 0, size);
	}
}
