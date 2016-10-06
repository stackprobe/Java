package schwarzer.shelves;

import charlotte.tools.IRect;

public class Button {
	public int width = 100;
	public String title = "未定義";
	public String method = "defaultButtonActionPerformed";

	// ---- for ShelvesDialog ----

	public IRect rect;
	public Header parent;
	public int index;

	// ----
}
