package charlotte_test.tools;

import charlotte.tools.IntTools;

public class IntToolsTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		System.out.println("" + IntTools.toUnsignedInt("00000000", 16, -9999));
		System.out.println("" + IntTools.toUnsignedInt("7fffffff", 16, -9999));
		System.out.println("" + IntTools.toUnsignedInt("80000000", 16, -9999));
		System.out.println("" + IntTools.toUnsignedInt("ffffffff", 16, -9999));
	}
}
