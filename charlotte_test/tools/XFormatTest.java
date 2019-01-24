package charlotte_test.tools;

public class XFormatTest {
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
		// ShelvesDialog moved
		/*
		XNode formatRoot = XNode.load(FileTools.readToEnd(ShelvesDialog.class.getResource("format/Design.xml")));
		XNode dataRoot = XNode.load("C:/var/DesignTest.xml");

		new XFormat(formatRoot).check(dataRoot);
		*/
	}
}
