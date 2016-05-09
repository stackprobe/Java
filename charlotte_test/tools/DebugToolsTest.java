package charlotte_test.tools;

import java.awt.Window;

import javax.swing.JDialog;

import charlotte.tools.DebugTools;

public class DebugToolsTest {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		JDialog dlg1 = new JDialog();
		JDialog dlg2 = new JDialog();
		JDialog dlg3 = new JDialog();

		for(Window win : DebugTools.getAllWindow()) {
			System.out.println("win: " + win);
		}
	}
}
