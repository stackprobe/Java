package charlotte.tools.shelves.shelf;

import java.awt.Component;

public abstract class Shelf {
	public int height;
	public int marginT;
	public int marginB;

	public int index;

	public abstract Component getComponent();

	public abstract void setValue(String value);
	public abstract String getValue();
}
