package charlotte_test.tools;

import java.io.InputStreamReader;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class FileToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			test04();

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
		test03_b("C:/");
		test03_b("C:/etc");
		test03_b("C:/etc/画像");
		test03_b("S:/");
		test03_b("S:\\");
		test03_b("//mimiko/pub");
		test03_b("\\\\mimiko\\pub");
		test03_b("H:/");
		test03_b("H:\\");
		test03_b("//no-exist-server/Projects");
		test03_b("\\\\no-exist-server\\Projects");
		test03_b("*invalid-path*");
		test03_b("D:/");
	}

	private static void test03_b(String dir) throws Exception {
		System.out.println("[" + dir + "] -> " + FileTools.getDiskFree(dir));
	}

	private static void test04() throws Exception {
		test04_b("");
		test04_b("\r\n");
		test04_b("改行無し1行");
		test04_b("改行有り1行\r\n");
		test04_b("1行目\r\n改行無し2行目");
		test04_b("1行目\r\n改行有り2行目\r\n");
		test04_b("1行目_2行目空行\r\n\r\n3行目\r\n");
	}

	private static void test04_b(String text) throws Exception {
		String tmpFile = FileTools.makeTempPath("{279889fe-0a85-49b0-b821-ca33a30294e7}");
		FileTools.del(tmpFile);
		FileTools.writeAllText(tmpFile, text, StringTools.CHARSET_UTF8);

		{
			InputStreamReader reader = null;
			try {
				reader = FileTools.readOpenTextFile(tmpFile, StringTools.CHARSET_UTF8);

				System.out.println("read-lines {");

				for(; ; ) {
					String line = FileTools.readLine(reader);

					if(line == null) {
						break;
					}
					System.out.println("line: [" + line + "]");
				}
				System.out.println("}");
			}
			finally {
				FileTools.close(reader);
			}
		}

		FileTools.del(tmpFile);
	}
}
