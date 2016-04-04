package charlotteLabo.mp4.test;

import charlotte.tools.FileTools;
import charlotteLabo.mp4.Box;

public class Test01 {
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
		//test01_b("C:/var/mp4/ddd.mp4");

		for(String file : FileTools.ls("C:/var/mp4")) {
			test01_b(file);
		}
	}

	private static void test01_b(String file) throws Exception {
		System.out.println("< " + file);

		Box root = Box.create(file);

		root.debugPrint();
	}
}
