package charlotte_test.tools;

import charlotte.tools.WindowsTools;

public class WindowsToolsTest {
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
		test01_b("A");
		test01_b("AA");
		test01_b("AAA");
		test01_b("tmp");
		test01_b("Tmp");
		test01_b("TMP");
		test01_b("temp");
		test01_b("Temp");
		test01_b("TEMP");
		test01_b("UserName");
		test01_b("UserProfile");
	}

	private static void test01_b(String name) throws Exception {
		System.out.println("[" + name + "] -> [" + WindowsTools.getEnv(name) + "]");
	}
}
