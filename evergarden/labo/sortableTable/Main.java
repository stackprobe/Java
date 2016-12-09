package evergarden.labo.sortableTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import charlotte.tools.AutoTable;
import charlotte.tools.CsvData;

public class Main {
	public static String[] titles;
	public static List<String[]> rows;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					//"javax.swing.plaf.metal.MetalLookAndFeel"
					//"com.sun.java.swing.plaf.motif.MotifLookAndFeel"
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
					//"com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"
					);

			String file;

			{
				JFileChooser fc = new JFileChooser("C:/var");

				fc.setFileFilter(new FileNameExtensionFilter("CSV ファイル (*.csv)", "csv"));
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), "default.csv"));
				fc.setDialogTitle("csvファイルを選択してね");

				int ret = fc.showOpenDialog(null);

				if(ret == JFileChooser.CANCEL_OPTION) {
					return;
				}
				if(ret != JFileChooser.APPROVE_OPTION) {
					throw new Exception("JFileChooser error!");
				}
				file = fc.getSelectedFile().getCanonicalPath();
			}

			{
				CsvData csv = new CsvData();
				csv.readFile(file);
				csv.trim();
				csv.toRect();
				AutoTable<String> table = csv.getTable();

				if(table.getHeight() < 2) {
					throw new Exception("ファイルの行が少なすぎます。");
				}
				if(table.getWidth() < 1) {
					throw new Exception("ファイルに列がありません。");
				}

				titles = table.getRow(0).toArray(new String[0]);
				rows = new ArrayList<String[]>();

				for(int rowidx = 1; rowidx < table.getHeight(); rowidx++) {
					rows.add(table.getRow(rowidx).toArray(new String[0]));
				}
			}

			new SortableTableDlg(null).perform();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
