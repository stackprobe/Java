package charlotte_test.test_01;

import charlotte.tools.MathTools;

public class Collatz {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() {
		test01(makeSrc());
	}

	private static String makeSrc() {
		return makeSrc(MathTools.random(100));
	}

	private static String makeSrc(int len) {
		StringBuffer buff = new StringBuffer();

		for(int c = 0; c < len; c++) {
			buff.append(MathTools.random("01"));
		}
		return buff.toString();
	}

	private static void test01(String src) {
		while(src.endsWith("0")) {
			src = src.substring(0, src.length() - 1);
		}
		for(; ; ) {
			System.out.println("[src]");
			System.out.println(src);

			while(src.startsWith("0")) {
				src = src.substring(1);
			}
			System.out.println("[d2]");
			System.out.println(src);

			if(src.length() <= 1) {
				break;
			}
			System.out.println("[add]");
			System.out.println(src);
			System.out.println(" " + src);
			System.out.println("1");

			String[] ret = add(src + "0", "1" + src, src.length() + 1);

			for(; ; ) {
				System.out.println("[added]");
				System.out.println(ret[0]);
				System.out.println(ret[1]);

				if(ret[1].indexOf('1') == -1) {
					break;
				}
				System.out.println("[re-add]");
				System.out.println(ret[0]);
				System.out.println(" " + ret[1]);

				ret = add(ret[0] + "0", "0" + ret[1], ret[0].length() + 1);

				while(ret[0].endsWith("0") && ret[1].endsWith("0")) {
					ret[0] = ret[0].substring(0, ret[0].length() - 1);
					ret[1] = ret[1].substring(0, ret[1].length() - 1);
				}
			}
			src = ret[0];
		}
	}

	private static String[] add(String s1, String s2, int len) {
		StringBuffer a = new StringBuffer();
		StringBuffer k = new StringBuffer();

		for(int index = 0; index < len; index++) {
			int v1 = s1.charAt(index) == '0' ? 0 : 1;
			int v2 = s2.charAt(index) == '0' ? 0 : 1;
			int v3 = v1 + v2;

			a.append(v3 % 2 == 0 ? '0' : '1');
			k.append(v3 / 2 == 0 ? '0' : '1');
		}
		return new String[] {
				a.toString(),
				k.toString(),
		};
	}
}
