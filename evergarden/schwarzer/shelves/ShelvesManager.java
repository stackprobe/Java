package evergarden.schwarzer.shelves;

import evergarden.schwarzer.shelves.shelf.DummyShelf;

public abstract class ShelvesManager {
	public abstract Form getForm();
	public abstract void load();

	public Package getShelfPackage() {
		return DummyShelf.class.getPackage();
	}
}
