package schwarzer.shelves;

import java.awt.Component;

import charlotte.tools.IRect;

public abstract class Shelf {
	public int height = 50;
	public String className = "DummyShelf";

	// ここへ追加...

	// ---- for ShelvesDialog ----

	public IRect rect;
	public Column parent;
	public int index;

	// ----

	public abstract Component getComponent();
	public abstract void setValue(String value);
	public abstract String getValue();
}
