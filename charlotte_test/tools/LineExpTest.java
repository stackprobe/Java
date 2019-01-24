package charlotte_test.tools;

import charlotte.tools.LineExp;

public class LineExpTest {
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
		System.out.println(LineExp.check("ABC<1,3,09>DEF", "ABC12DEF")); // true
		System.out.println(LineExp.check("ABC<1,3,09>DEF", "ABC123DEF")); // true
		System.out.println(LineExp.check("ABC<1,3,09>DEF", "ABC1234DEF")); // false
	}
}
