package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;

public class Tab {
	public int marginL = 10;
	public int marginT = 10;
	public int marginR = 10;
	public int marginB = 10;
	public int colSpan = 10;
	public String title = "未定義";

	// ここへ追加...

	// ---- children ----

	public List<Column> columns = new ArrayList<Column>();

	// ---- for ShelvesDialog ----

	public IRect outernal;
	public IRect internal;
	public Form parent;
	public int index;

	// ----
}
