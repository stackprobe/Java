package charlotte.tools.shelves;

import java.util.List;

import charlotte.tools.shelves.shelf.Shelf;

public abstract class ShelvesGround {
	public int width;
	public int height;

	public ShelvesDlg dlg;
	public List<Shelf> shelves;

	public abstract void load();
	public abstract void save();
}
