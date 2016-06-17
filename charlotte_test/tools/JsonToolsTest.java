package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.JsonTools;

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
		test01_a("C:/var/JsonTest01.txt");
		test01_a("C:/var/JsonTest01u16.txt");
		test01_a("C:/var/JsonTest01u32.txt");
	}

	private static void test01_a(String file) throws Exception {
		System.out.println("file: " + file); // test
		Object root = JsonTools.decode(FileTools.readAllBytes(file));
		System.out.println("root: " + root); // test
		String json = JsonTools.encode(root);
		System.out.println("json: " + json); // test
	}
}
