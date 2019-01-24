package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.WindowsTools;

public class WindowsToolsTest {
	public static void main(String[] args) {
		try {
			test01();
			test02();

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

	private static void test02() throws Exception {
		test02_b("C:/");
		test02_b("C:\\");
		test02_b("C:/var");
		test02_b("C:\\var");

		test02_b("C:\\not-exist-dir"); // 存在しないパス -> { "" }
		test02_b("C:\\var\\画像4x1000.zip"); // 存在するファイル -> { "ローカル名" }

		test02_b("\\");
		test02_b("\\.");
		test02_b(".");
		test02_b("..");
	}

	private static void test02_b(String dir) throws Exception {
		String outFile = FileTools.makeTempPath();

		System.out.println(dir + " ...");

		WindowsTools.ls(dir, outFile);

		for(String lPath : FileTools.readAllLines(outFile, StringTools.CHARSET_SJIS)) {
			System.out.println(dir + " > " + lPath);
		}
	}
}
