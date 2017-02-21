package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.JsonTools;
import charlotte.tools.StringTools;

public class JsonToolsTest {
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

	public static void test01() throws Exception {
		test01_a("C:/var/Setting_template.json");
	}

	private static void test01_a(String file) throws Exception {
		System.out.println("file: " + file); // test

		byte[] rJson = FileTools.readAllBytes(file);
		Object root = JsonTools.decode(rJson);
		String swJson = JsonTools.encode(root);

		System.out.println("json: " + swJson); // test

		byte[] wJson = swJson.getBytes(StringTools.CHARSET_UTF8);

		// ---- 2 ----

		Object root2 = JsonTools.decode(wJson);
		String swJson2 = JsonTools.encode(root2);
		byte[] wJson2 = swJson2.getBytes(StringTools.CHARSET_UTF8);

		// ----

		FileTools.writeAllBytes("C:/temp/JsonToolsTest_" + _test01_a_serial + "_1.txt", rJson);
		FileTools.writeAllBytes("C:/temp/JsonToolsTest_" + _test01_a_serial + "_2.txt", wJson);
		FileTools.writeAllBytes("C:/temp/JsonToolsTest_" + _test01_a_serial + "_3.txt", wJson2);
		_test01_a_serial++;
	}

	private static int _test01_a_serial = 0;
}
