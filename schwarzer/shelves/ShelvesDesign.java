package schwarzer.shelves;

import charlotte.tools.FileTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;

public abstract class ShelvesDesign extends ShelvesMaster {
	private XNode _root;

	public ShelvesDesign(XNode root) throws Exception {
		_root = root;
		init();
	}

	private static XFormat _format = null;

	private void init() throws Exception {
		if(_format == null) {
			_format = new XFormat(XNode.load(FileTools.readToEnd(ShelvesDesign.class.getResource(
					"format/Design.xml"
					))));
		}
		_format.check(_root);
	}

	private Form _form = null;

	@Override
	public Form getForm() {
		if(_form == null) {
			_form = createForm();
		}
		return _form;
	}

	private Form createForm() {
		throw null; // TODO
	}
}
