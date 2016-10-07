package schwarzer.shelves;

import schwarzer.shelves.shelf.DummyShelf;

public abstract class ShelvesManager {
	public abstract Form getForm();
	public abstract void load();
	public abstract void save();

	public Package getShelfPackage() {
		return DummyShelf.class.getPackage();
	}
}
