package charlotte_test.saber;

import charlotte.saber.FilingCase3;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class FilingCase3Test {
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
		FilingCase3 fc3 = new FilingCase3("localhost", 65123, "Test");
		try {
			fc3.post("a/b/c.txt", "Hello".getBytes(StringTools.CHARSET_ASCII));
			fc3.post("a/b/d.txt", "World".getBytes(StringTools.CHARSET_ASCII));
			fc3.post("a/b/1/2/3", "123".getBytes(StringTools.CHARSET_ASCII));

			System.out.println(new String(fc3.get("a/b/c.txt"), StringTools.CHARSET_ASCII));
			System.out.println(new String(fc3.get("a/b/d.txt"), StringTools.CHARSET_ASCII));

			for(String lPath : fc3.list("a/b")) {
				System.out.println(lPath);
			}
			fc3.delete("a/b/1");

			for(String lPath : fc3.list("a/b")) {
				System.out.println(lPath);
			}
			System.out.println(new String(fc3.getPost("a/b/1/123.txt", "123".getBytes(StringTools.CHARSET_ASCII)), StringTools.CHARSET_ASCII));
			System.out.println(new String(fc3.getPost("a/b/1/123.txt", "456".getBytes(StringTools.CHARSET_ASCII)), StringTools.CHARSET_ASCII));
			System.out.println(new String(fc3.getPost("a/b/1/123.txt", "789".getBytes(StringTools.CHARSET_ASCII)), StringTools.CHARSET_ASCII));
		}
		finally {
			FileTools.close(fc3);
		}
	}
}
