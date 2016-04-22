package charlotte_test.tools;

import charlotte.tools.LongTools;

public class LongToolsTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		System.out.println(LongTools.toString0x(LongTools.revEndian(0x8899aabbccddeeffL)));
		System.out.println(LongTools.toString0x(LongTools.revEndian(0xffeeddccbbaa9988L)));
	}
}
