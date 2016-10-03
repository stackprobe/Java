package charlotte.tools.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.shelves.shelf.Shelf;

public class ShelvesDlg {
	private List<Package> _shelfPackages = new ArrayList<Package>();
	private Design _design = null;

	public void addShelfDefaultPackage() {
		addShelfPackage(Shelf.class.getPackage());
	}

	public void addShelfPackage(Package pkg) {
		_shelfPackages.add(pkg);
	}

	public void setStructure(Design design) {
		_design = design;
	}

	public void perform() throws Exception {
	}
}
