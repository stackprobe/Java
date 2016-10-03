package charlotte.tools.shelves;

import charlotte.tools.FileTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;

public class Design {
	private XNode _root;

	public Design(XNode root) throws Exception {
		new XFormat(XNode.load(FileTools.readToEnd(Design.class.getResource("res/Design.xml")))).check(root);
		_root = root;
	}
}
