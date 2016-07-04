package charlotte_test.tools;

import charlotte.tools.FileTools;

public class FileToolsTest {
	public static void main(String[] args) {
		try {
			test01();
			//test02();
			test03();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		test01_b(null);
		test01_b("");
		test01_b(" ");
		test01_b("$");
		test01_b("$ ");
		test01_b(" $");
		test01_b("<");
		test01_b("NUL");
		test01_b("nul");
		test01_b("MUL");
		test01_b("mul");
		test01_b("NUL.a.b.c");
		test01_b("a.b.c.NUL");
		test01_b("...");
		test01_b(".aaa.");
		test01_b("a...a");
		test01_b("<aaa>");
		test01_b("a<<<a");
		test01_b(" a . b . c ");
	}

	private static void test01_b(String str) {
		System.out.println(str + " -> " + FileTools.toFairLocalPath(str));
	}

	private static void test02() {
		for(String path : FileTools.lss("C:/Dev/CSharp")) {
			System.out.println(path);
		}
	}

	private static void test03() throws Exception {
		test03_b("C:\\");
		test03_b("C:\\etc");
		test03_b("C:\\etc\\画像");
		test03_b("S:\\");
		test03_b("\\\\mimiko\\pub");
	}

	private static void test03_b(String dir) throws Exception {
		System.out.println("[" + dir + "] -> " + FileTools.getDiskFree(dir));
	}
}
