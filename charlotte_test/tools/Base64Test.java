package charlotte_test.tools;

import charlotte.tools.Base64;
import charlotte.tools.StringTools;

public class Base64Test {
	public static final void main(String[] args) {
		try {
			test01(StringTools.hex(""));
			test01(StringTools.hex("de"));
			test01(StringTools.hex("dead"));
			test01(StringTools.hex("deadbe"));
			test01(StringTools.hex("deadbeaf"));
			test01(StringTools.hex("00112233445566778899"));
			test01(StringTools.hex("00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff"));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01(byte[] src) {
		System.out.println("src: " + StringTools.toHex(src));
		String enc = Base64.getString(src);
		System.out.println("enc: " + enc);
		byte[] dec = Base64.getBytes(enc);
		System.out.println("dec: " + StringTools.toHex(dec));
	}
}
