package charlotte_test.tools;

import charlotte.tools.ZipTools;

public class ZipToolsTest {
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
		ZipTools.extract("S:/_hidden/Factory.zip", "C:/temp/Factory");
		ZipTools.pack("C:/temp/Factory", "C:/temp/Factory.zip", "test/Factory(Repack)");
	}
}
