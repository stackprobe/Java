package charlotte_test.tools;

import charlotte.tools.IntTools;
import charlotte.tools.Xorshift;

public class XorshiftTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		Xorshift xSft = new Xorshift(1, 0, 0, 0, 0);

		for(int c = 0; c < 1000; c++) {
			System.out.println(IntTools.toUHex(xSft.next()));
		}
	}
}
