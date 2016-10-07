package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;
import schwarzer.shelves.ShelvesDialog;

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
		XNode formatRoot = XNode.load(FileTools.readToEnd(ShelvesDialog.class.getResource("format/Design.xml")));
		XNode dataRoot = XNode.load("C:/var/DesignTest.xml");

		new XFormat(formatRoot).check(dataRoot);
	}
}
