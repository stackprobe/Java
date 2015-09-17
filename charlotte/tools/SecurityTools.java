package charlotte.tools;

import java.security.MessageDigest;

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
}
