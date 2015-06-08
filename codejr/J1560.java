package codejr;

import charlotte.tools.IntTools;
import charlotte.tools.SystemTools;

public class J1560 {
	public static void main(String[] args) {
		try {
			//test01();
			//test02(IntTools.pow(10, 5));
			//test02(IntTools.pow(10, 9));
			test03(IntTools.pow(10, 5));
			test03(IntTools.pow(10, 9));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		for(int n = 1; n < 100; n++) {
			test02(n);
		}
		for(int n = 100; n < 10000; n += 100) {
			test02(n);
		}
		for(int c = 0; c < 100; c++) {
			int n = SystemTools.random(1000000) + 1;
			test02(n);
		}
		for(int c = 0; c < 100; c++) {
			int n = SystemTools.random(100000000) + 1;
			test02(n);
		}
	}

	private static void test02(int n) throws Exception {
		System.out.println("n: " + n);

		long ans = performTest(n);
		long ret = perform(n);

		System.out.println("ans: " + ans);
		System.out.println("ret: " + ret);

		if(ans != ret) {
			throw new Exception("ng");
		}
	}

	private static long performTest(int n) {
		long t = 0;

		for(int c = 1; c <= n; c++) {
			if(c % 3 == 0 ||
					c % 5 == 0 ||
					c % 7 == 0 ||
					c % 11 == 0 ||
					c % 13 == 0 ||
					c % 17 == 0 ||
					c % 19 == 0 ||
					c % 23 == 0 ||
					c % 29 == 0 ||
					c % 31 == 0
					)
				t += c;
		}
		return t;
	}

	private static void test03(int n) {
		System.out.println(n + " ==> " + perform(n));
	}

	private static final int[] P = new int[]{ 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
	private static int _n;
	private static long _ret;

	private static long perform(int n) {
		_n = n;
		_ret = 0;
		mulP(1, 1, 0);
		return _ret;
	}

	private static void mulP(long c, int sign, int index) {
		for(; index < P.length; index++) {
			long cc = c * P[index];
			_ret += sign * mulTotal(cc);
			mulP(cc, -sign, index + 1);
		}
	}

	private static long mulTotal(long c) {
		long ret = _n;
		ret /= c;
		ret *= ret + 1;
		ret /= 2;
		ret *= c;
		return ret;
	}
}
