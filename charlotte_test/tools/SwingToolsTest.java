package charlotte_test.tools;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import charlotte.tools.FileTools;
import charlotte.tools.SwingTools;

public class SwingToolsTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		Component parent = null;
		String dir;
		String file;

		// ディレクトリ選択
		for(; ; ) {
			{
				JFileChooser fc = new JFileChooser("C:/var");

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("ディレクトリを選択してね");

				int ret = fc.showOpenDialog(parent);

				if(ret == JFileChooser.CANCEL_OPTION) {
					return;
				}
				if(ret != JFileChooser.APPROVE_OPTION) {
					throw new Exception("JFileChooser error!");
				}
				dir = fc.getSelectedFile().getCanonicalPath();
			}

			/*
			if(FileTools.getExt(dir).equalsIgnoreCase("csv") == false) {
				JOptionPane.showMessageDialog(
						parent,
						"extension is not csv",
						"CSV janai!",
						JOptionPane.WARNING_MESSAGE
						);
				//continue;
			}
			*/
			break;
		}

		// ファイル選択
		for(; ; ) {
			{
				JFileChooser fc = new JFileChooser(dir);

				fc.setFileFilter(new FileNameExtensionFilter("CSV ファイル (*.csv)", "csv"));
				fc.setSelectedFile(new File(dir, "default.csv"));
				//fc.setSelectedFile(new File(fc.getCurrentDirectory(), "default.csv"));
				fc.setDialogTitle("ファイルを選択してね");

				int ret = fc.showOpenDialog(parent);

				if(ret == JFileChooser.CANCEL_OPTION) {
					return;
				}
				if(ret != JFileChooser.APPROVE_OPTION) {
					throw new Exception("JFileChooser error!");
				}
				file = fc.getSelectedFile().getCanonicalPath();
			}

			if(FileTools.getExt(file).equalsIgnoreCase("csv") == false) {
				JOptionPane.showMessageDialog(
						parent,
						"extension is not csv",
						"CSV janai!",
						JOptionPane.WARNING_MESSAGE
						);
				//continue;
			}
			break;
		}

		System.out.println("selected file1: " + file);

		// (出力)ファイル選択
		{
			JFileChooser fc = new JFileChooser();

			fc.setFileFilter(new FileNameExtensionFilter("CSV ファイル (*.csv)", "csv"));
			fc.setSelectedFile(new File(dir, FileTools.getLocal(file)));
			//fc.setSelectedFile(new File(fc.getCurrentDirectory(), FileTools.getLocal(file)));
			fc.setDialogTitle("(出力)ファイルを入力してね");

			int ret = SwingTools.showSaveDialogConfirmOverwrite(fc, parent);

			if(ret == JFileChooser.CANCEL_OPTION) {
				return;
			}
			if(ret != JFileChooser.APPROVE_OPTION) {
				throw new Exception("JFileChooser error!");
			}
			file = fc.getSelectedFile().getCanonicalPath();
		}

		System.out.println("selected file2: " + file);
	}
}
