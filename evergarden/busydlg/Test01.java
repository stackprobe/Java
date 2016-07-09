package evergarden.busydlg;

import javax.swing.JOptionPane;

import charlotte.tools.FileTools;

public class Test01 {
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
		BusyDlg bd = null;

		try {
			JOptionPane.showMessageDialog(
					null,
					"はじまるで。",
					"情報",
					JOptionPane.INFORMATION_MESSAGE
					);

			bd = new BusyDlg("だいたい15秒くらい待つのやで...");

			for(int c = 15; 0 < c; c--) {
				System.out.println("あと " + c + " 秒...");
				Thread.sleep(1000);
			}
			FileTools.close(bd);
			bd = null;

			JOptionPane.showMessageDialog(
					null,
					"おわったで。",
					"情報",
					JOptionPane.INFORMATION_MESSAGE
					);
		}
		finally {
			FileTools.close(bd);
			bd = null;
		}
	}
}
