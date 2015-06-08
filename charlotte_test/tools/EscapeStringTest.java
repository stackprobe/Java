package charlotte_test.tools;

import charlotte.tools.EscapeString;
import charlotte.tools.StringTools;

public class EscapeStringTest {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		for(int c = 0; c < 1000; c++) {
			test(StringTools.random("123ABCabc日本語\t\r\n $", 0, 100));
		}
		System.out.println("OK!");
	}

	private static void test(String src) {
		System.out.println("src: " + src);
		String enc = EscapeString.i.encode(src);
		System.out.println("enc: " + enc);
		String dec = EscapeString.i.decode(enc);
		System.out.println("dec: " + dec);

		if(src.equals(dec) == false) {
			throw new RuntimeException("ng");
		}
	}
}
