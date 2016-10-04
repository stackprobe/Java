package charlotte.tools.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.XFormat;
import charlotte.tools.XNode;
import charlotte.tools.shelves.shelf.Shelf;

public class ShelvesDlg {
	private List<Package> _shelfPackages = new ArrayList<Package>();
	private XNode _design = null;
	private ShelvesGround _gnd = null;

	public ShelvesDlg() {
		init();
	}

	public ShelvesDlg(XNode design, ShelvesGround gnd) {
		init();
		setDesign(design);
		setGround(gnd);
	}

	private void init() {
		addShelfPackage(Shelf.class.getPackage());
	}

	public void addShelfPackage(Package pkg) {
		_shelfPackages.add(pkg);
	}

	public void setDesign(XNode design) {
		_design = design;
	}

	public void setGround(ShelvesGround gnd) {
		_gnd = gnd;
	}

	private XFormat _designFormat = null;

	public void perform() throws Exception {
		if(_designFormat == null) {
			_designFormat = new XFormat(
					XNode.load(FileTools.readToEnd(ShelvesDlg.class.getResource("res/Design.xml")))
					);
		}
		_designFormat.check(_design);

		// TODO
	}
}
