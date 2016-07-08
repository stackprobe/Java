package evergarden.busydlg;

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
			bd = new BusyDlg("だいたい15秒くらい表示されます...");

			for(int c = 15; 0 < c; c--) {
				System.out.println("あと " + c + " 秒...");
				Thread.sleep(1000);
			}
		}
		finally {
			FileTools.close(bd);
			bd = null;
		}
	}
}
