package evergarden.labo;

import charlotte.tools.TimeData;

public class Test01 {
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
		long st = System.currentTimeMillis();

		for(int c = 0; c < 1000000; c++) {
			TimeData t = new TimeData(1);
			t.toString();
		}
		long et = System.currentTimeMillis();
		long dt = et - st;

		System.out.println("" + dt);
	}
}
