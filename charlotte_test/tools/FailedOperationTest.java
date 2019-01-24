package charlotte_test.tools;

import javax.swing.UIManager;

import charlotte.tools.Cancelled;
import charlotte.tools.Completed;
import charlotte.tools.FailedOperation;

public class FailedOperationTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
					);

			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		test01(Completed.i);
		test01(Cancelled.i);
		test01(FailedOperation.i);
		test01(new FailedOperation("ぎねすびあ0"));
		test01(new Exception());
		test01(new Exception("ぎねすびあ"));
		test01(new RuntimeException());
		test01(new RuntimeException("ぎねすびあ2"));
		test01(new Error());
		test01(new Error("ぎねすびあ2"));
		test01(new Error("ぎねすびあ2", new Error("いんなー2", new Error("インナー3"))));
	}

	private static void test01(Throwable e) {
		//*
		try {
			throw e;
		}
		catch(Throwable ex) {
			FailedOperation.caught(null, ex, "ほっとわいん");
		}
		/*/
		FailedOperation.caught(null, e, "ほっとわいん");
		//*/
	}
}
