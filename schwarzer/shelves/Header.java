package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;

public class Header {
	public static final int ALIGN_L = 1;
	public static final int ALIGN_M = 2;
	public static final int ALIGN_R = 3;

	public int height = 50;
	public int marginL = 10;
	public int marginT = 10;
	public int marginR = 10;
	public int marginB = 10;
	public int buttonSpan = 10;
	public int align = ALIGN_M;

	// ---- children ----

	public List<Button> buttons = new ArrayList<Button>();

	// ---- for ShelvesDialog ----

	public IRect outernal;
	public IRect internal;
	public Form parent;
	public int index;

	// ----
}
