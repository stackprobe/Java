package charlotte_test.tools;

import charlotte.tools.JDate;

public class JDateTest {
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

	private static void test01() throws Exception {
		test01(1999, 7, 15);
		test01(1970, 10, 1);
		test01(1868, 1, 25);
		test01(1868, 1, 24);
		test01(1868, 1, 1);
		test01(1867, 12, 31);
		test01(1912, 7, 29);
		test01(1912, 7, 30);
		test01(9999, 12, 31);

		for(JDate.Period period : JDate.getPeriods()) {
			System.out.println(period.firstDate + " - " + period.lastDate);
		}
		for(String eyy : JDate.getEYYList()) {
			System.out.println("EYY: " + eyy);
		}
		for(String e : JDate.getEList()) {
			System.out.println("E: " + e);
		}
		test02("H01");
		test02("H02/12/31");
		test02("Puppu-!"); // -> null
		test02("S50.5.5");
		test02("昭和５０年５月５日");
		test02("明治元年3/3");
	}

	private static void test01(int y, int m, int d) {
		JDate date = new JDate();

		date.y = y;
		date.m = m;
		date.d = d;

		date.gggn();

		System.out.println(date.gg + "/" + date.g + "/" + date.nen);
	}

	private static void test02(String str) {
		System.out.println(str + " -> " + JDate.forWareki(str));
	}
}
