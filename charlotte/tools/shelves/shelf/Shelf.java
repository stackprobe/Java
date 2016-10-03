package charlotte.tools.shelves.shelf;

import java.awt.Component;

public abstract class Shelf {
	public String name;
	public int height;
	public double rHeight;

	public abstract Component getComponent();

	public abstract void setValue(String value);
	public abstract String getValue();
}
