package codeiq;

import charlotte.tools.StringTools;

public class J1577 {
	public static void main(String[] args) {
		try {
			test01(
					"((2 * 3) - 5 + 1) << 2",
					"2 * 3 - 5 + 1 << 2"
					);
			test01(
					"12 | (16 >> ((2 + (4 % 3))))",
					"12 | 16 >> 2 + 4 % 3"
					);
			test01(
					"((7 & 13) + 4 / (3 + ((5 << 2) | 3)))",
					"(7 & 13) + 4 / (3 + (5 << 2 | 3))"
					);

			// ----

			// 結合規則は全て左から右
			// + - は順序が変わっても良さそうだけど、順序が変わらないようにする。

			test01(
					"1 + (2 + 3) + 4",
					"1 + (2 + 3) + 4" // 2 + 3 を先に!
					);
			test01(
					"1 * (2 + 3) + 4",
					"1 * (2 + 3) + 4"
					);
			test01(
					"1 + (2 * 3) + 4",
					"1 + 2 * 3 + 4"
					);
			test01(
					"1 * (2 * 3) + 4",
					"1 * (2 * 3) + 4" // 2 * 3 を先に!
					);
			test01(
					"1 + (2 + 3) * 4",
					"1 + (2 + 3) * 4" // 2 + 3 を先に!
					);
			test01(
					"1 * (2 + 3) * 4",
					"1 * (2 + 3) * 4"
					);
			test01(
					"1 + (2 * 3) * 4",
					"1 + 2 * 3 * 4"
					);
			test01(
					"1 * (2 * 3) * 4",
					"1 * (2 * 3) * 4" // 2 * 3 を先に!
					);

			// ----

			test01("(((((1)))))", "1");
			test01(
					"(1 * (2 & 3) / 4) + 999",
					"1 * (2 & 3) / 4 + 999"
					);

			// ----

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01(String str, String ans) throws Exception {
		System.out.println("str: " + str); // test
		System.out.println("ans: " + ans); // test

		String ret = perform(str);

		System.out.println("ret: " + ret); // test

		if(ans.equals(ret) == false) {
			throw new Exception("ng");
		}
	}

	private static String perform(String str) {
		for(int index = 0; index < str.length(); index++) {
			if(str.charAt(index) == '(') {
				int lwPrio = getLwPrio(str, index + 1);
				int lPrio = getLeftPrio(str, index - 1);
				int rPrio = getRightPrio(str, _endPos + 1);

				if(lwPrio == -1 || lPrio < lwPrio && rPrio <= lwPrio) {
					str = StringTools.remove(str, _endPos);
					str = StringTools.remove(str, index);
					index--;
				}
			}
		}
		return str;
	}

	private static int _endPos;

	private static int getLwPrio(String str, int index) {
		int bDeep = 0;
		int ret = -1;

		for(; ; index++) {
			char chr = str.charAt(index);

			if(chr == '(') {
				bDeep++;
			}
			else if(chr == ')') {
				if(bDeep == 0) {
					break;
				}
				bDeep--;
			}
			else if(bDeep == 0) {
				int prio = getPrio(chr);

				if(prio != -1 && (ret == -1 || prio < ret)) {
					ret = prio;
				}
			}
		}
		_endPos = index;
		return ret;
	}

	private static int getLeftPrio(String str, int index) {
		int ret = -1;

		for(; ret == -1 && 0 <= index; index--) {
			char chr = str.charAt(index);

			if(chr == '(') {
				break;
			}
			ret = getPrio(chr);
		}
		return ret;
	}

	private static int getRightPrio(String str, int index) {
		int ret = -1;

		for(; ret == -1 && index < str.length(); index++) {
			char chr = str.charAt(index);

			if(chr == ')') {
				break;
			}
			ret = getPrio(chr);
		}
		return ret;
	}

	private static int getPrio(char op) {
		switch(op) {
		case '*':
		case '/':
		case '%':
			return 6;
		case '+':
		case '-':
			return 5;
		case '<':
		case '>':
			return 4;
		case '&':
			return 3;
		case '^':
			return 2;
		case '|':
			return 1;
		}
		return -1;
	}
}
