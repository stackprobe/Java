package evergarden.schwarzer.shelves;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

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

	// ---- init @ ShelvesDialog.init() ----

	public IntRect outernal;
	public IntRect internal;
	public Form parent;
	public int index;

	// ---- work @ ShelvesDialog.inti() ----

	public JScrollPane scroll;

	// ----
}
