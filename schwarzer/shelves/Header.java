package schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.IRect;
import charlotte.tools.StringTools;

public class Header {
	public int height = 50;
	public int marginL = 10;
	public int marginT = 10;
	public int marginR = 10;
	public int marginB = 10;
	public int buttonSpan = 10;
	public String align = "center";

	// ここへ追加...

	// ---- children ----

	public List<Button> buttons = new ArrayList<Button>();

	// ---- for ShelvesDialog ----

	public IRect outernal;
	public IRect internal;
	public Form parent;
	public int index;

	// ----

	public boolean alignLeft() {
		return StringTools.startsWithIgnoreCase(align, "L");
	}

	public boolean alignMiddle() {
		return alignLeft() == false && alignRight() == false;
	}

	public boolean alignRight() {
		return StringTools.startsWithIgnoreCase(align, "R");
	}
}
