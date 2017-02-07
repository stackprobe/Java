package evergarden.schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

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

	// ---- init @ ShelvesDialog.init() ----

	public IntRect outernal;
	public IntRect internal;
	public Form parent;
	public int index;

	// ----

	private int getAlign() {
		if(StringTools.startsWithIgnoreCase(align, "L")) {
			return 1;
		}
		if(StringTools.startsWithIgnoreCase(align, "R")) {
			return 3;
		}
		return 2;
	}

	public boolean alignLeft() {
		return getAlign() == 1;
	}

	public boolean alignMiddle() {
		return getAlign() == 2;
	}

	public boolean alignRight() {
		return getAlign() == 3;
	}
}
