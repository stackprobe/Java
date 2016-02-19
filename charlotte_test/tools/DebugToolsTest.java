package charlotte_test.tools;

import java.awt.Window;

import javax.swing.JDialog;

import charlotte.tools.DebugTools;

public class DebugToolsTest {
	public static void main(String[] args) {
		try {
			{
				JDialog dlg1 = new JDialog();
				JDialog dlg2 = new JDialog();
				JDialog dlg3 = new JDialog();

				for(Window win : DebugTools.getAllWindow()) {
					System.out.println("win: " + win);
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
