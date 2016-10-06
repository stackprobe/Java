package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;

public class Form {
	public int width = 640;
	public int height = 480;
	public int marginL = 10;
	public int marginT = 10;
	public int marginR = 10;
	public int marginB = 10;
	public String title = "未定義";

	// ---- children ----

	public Header header = null;
	public Header footer = null;
	public List<Tab> tabs = new ArrayList<Tab>();

	// ---- for ShelvesDialog ----

	public IRect outernal;
	public IRect internal;
	public IRect tabRect;

	// ----
}
