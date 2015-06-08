package charlotte_test.tools;

import charlotte.tools.TimeData;
import charlotte.tools.XNode;

public class XNodeTest {
	public static void main(String[] args) {
		try {
			XNode root = XNode.load("C:/tmp/test.xml");
			debugPrint(root);
			test01(root);
			root.save("C:/temp/test_out.xml");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void debugPrint(XNode node) {
		System.out.println("name=[" + node.getName() + "]");
		System.out.println("value=[" + node.getValue() + "]");
		System.out.println("children=[");

		for(XNode child : node.getChildren()) {
			debugPrint(child);
		}
		System.out.println("]");
	}

	private static void test01(XNode root) throws Exception {
		if("dateAndTime".equals(root.getName())) {
			System.out.println("dateAndTime: " + root.getValue() + " -> " + TimeData.parseISO8061(root.getValue()));
		}

		// ----

		for(XNode child : root.getChildren()) {
			test01(child);
		}
	}
}
