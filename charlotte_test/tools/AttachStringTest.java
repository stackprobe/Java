package charlotte_test.tools;

import java.util.List;

import charlotte.tools.AttachString;
import charlotte.tools.DebugTools;
import charlotte.tools.StringTools;

public class AttachStringTest {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() {
		for(int c = 0; c < 1000; c++) {
			test(StringTools.random("123ABCabc日本語\t\r\n :$.", 0, 100, 0, 20));
		}
		System.out.println("OK!");
	}

	private static void test(List<String> src) {
		DebugTools.printList("src", src);
		String enc = AttachString.i.untokenize(src);
		System.out.println("enc: " + enc);
		List<String> dec = AttachString.i.tokenize(enc);
		DebugTools.printList("dec", dec);

		if(StringTools.isSame(src, dec) == false) {
			throw new RuntimeException("ng");
		}
	}
}
