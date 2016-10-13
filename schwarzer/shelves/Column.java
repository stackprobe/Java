package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;

public class Column {
	public int width = 500;
	public int shelfSpan = 10;

	// ここへ追加...

	// ---- children ----

	public List<Shelf> shelves = new ArrayList<Shelf>();

	// ---- init @ ShelvesDialog.init() ----

	public IRect rect;
	public Tab parent;
	public int index;

	// ----
}
