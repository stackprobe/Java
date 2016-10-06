package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;

public class Column {
	public int width = 600;
	public int shelfSpan = 10;

	// ---- children ----

	public List<Shelf> shelves = new ArrayList<Shelf>();

	// ---- for ShelvesDialog ----

	public IRect rect;
	public Tab parent;
	public int index;

	// ----
}
