package charlotte_test.tools;

import charlotte.tools.Cancelled;
import charlotte.tools.Completed;
import charlotte.tools.FaultOperation;

public class FaultOperationTest {
	public static void main(String[] args) {
		try {
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
		test01(FaultOperation.i);
		test01(new FaultOperation("ぎねすびあ0"));
		test01(new Exception());
		test01(new Exception("ぎねすびあ"));
		test01(new RuntimeException());
		test01(new RuntimeException("ぎねすびあ2"));
		test01(new Error());
		test01(new Error("ぎねすびあ2"));
	}

	private static void test01(Throwable e) {
		//*
		try {
			throw e;
		}
		catch(Throwable ex) {
			FaultOperation.caught(null, ex, "ほっとわいん");
		}
		/*/
		FaultOperation.caught(null, e, "ほっとわいん");
		//*/
	}
}
