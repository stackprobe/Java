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

	public static byte[] getSHA512(SubBytes block) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(block.getMaster(), block.getStartPos(), block.size());
		byte[] ret = md.digest();
		return ret;
	}

	private static Random _cRandom = new SecureRandom();

	public static int cRandom(int modulo) {
		return _cRandom.nextInt(modulo);
	}

	public static int cRandom(int minval, int maxval) {
		return cRandom(maxval + 1 - minval) + minval;
	}

	public static byte[] cRandSq(byte[] dest, int startPos, int size) {
		for(int index = 0; index < size; index++) {
			dest[startPos + index] = (byte)_cRandom.nextInt(256);
		}
		return dest;
	}

	public static byte[] cRandSq(int size) {
		return cRandSq(new byte[size], 0, size);
	}

	public static char cRandom(String chrs) {
		return chrs.charAt(cRandom(chrs.length()));
	}

	public static String cRandHex() {
		return cRandHex(32);
	}

	public static String cRandHex(int len) {
		return makePassword(StringTools.hexadecimal, len);
	}

	public static String makePassword(String chrs, int len) {
		StringBuffer buff = new StringBuffer();

		for(int c = 0; c < len; c++) {
			buff.append(cRandom(chrs));
		}
		return buff.toString();
	}

	public static String makePassword() {
		return makePassword(StringTools.DIGIT + StringTools.ALPHA + StringTools.alpha, 22); // log 2,(62^22) == 130.*
	}

	public static String makePasswordUpper() {
		return makePassword(StringTools.DIGIT + StringTools.ALPHA, 25); // log 2,(36^25) == 129.*
	}

	public static String makePasswordLower() {
		return makePassword(StringTools.DIGIT + StringTools.alpha, 25); // log 2,(36^25) == 129.*
	}

	public static String makePasswordDigit() {
		return makePassword(StringTools.DIGIT, 40); // log 2,(10^40) == 132.*
	}

	public static String makePasswordAlpha() {
		return makePassword(StringTools.ALPHA + StringTools.alpha, 23); // log 2,(52^23) == 131.*
	}

	public static String makePasswordUpperOnly() {
		return makePassword(StringTools.ALPHA, 28); // log 2,(26^28) == 131.*
	}

	public static String makePasswordLowerOnly() {
		return makePassword(StringTools.alpha, 28); // log 2,(26^28) == 131.*
	}
}
