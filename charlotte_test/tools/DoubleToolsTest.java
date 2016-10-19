package charlotte_test.tools;

import charlotte.tools.DoubleTools;

public class DoubleToolsTest {
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
		test01(Double.NaN);
		test01(0.0);
		test01(1.0);
		test01(-1.0);
		test01(0.5);
		test01(-0.5);
		test01(1.0 / 3.0);
		test01(1.23e100);
		test01(1.23e-100);
		test01(-1.23e100);
		test01(-1.23e-100);
	}

	private static void test01(double value) {
		System.out.println(value + " -> " + DoubleTools.toString(value));
	}
}
