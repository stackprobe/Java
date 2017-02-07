package evergarden.schwarzer.shelves;

public class Button {
	public int width = 100;
	public String title = "未定義";
	public String method = "defaultButtonActionPerformed";

	// ここへ追加...

	// ---- init @ ShelvesDialog.init() ----

	public IntRect rect;
	public Header parent;
	public int index;

	// ----
}
