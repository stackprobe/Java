package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;
import charlotte.tools.shelves.ShelvesDlg;

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
		XNode formatRoot = XNode.load(FileTools.readToEnd(ShelvesDlg.class.getResource("res/Design.xml")));
		XNode dataRoot = XNode.load("C:/var/DesignTest.xml");

		new XFormat(formatRoot).check(dataRoot);
	}
}
