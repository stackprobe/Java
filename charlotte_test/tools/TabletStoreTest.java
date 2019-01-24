package charlotte_test.tools;

import charlotte.tools.TabletStore;
import charlotte.tools.ThreadTools;

public class TabletStoreTest {
	public static void main(String[] args) {
		try {
			test00();
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test00() throws Exception {
		Thread th = new Thread() {
			@Override
			public void run() {
			}
		};
		th.start();

		Thread.sleep(500);

		th.interrupt(); // 終わってるスレッドに interrupt(); しても問題無い。
		th.interrupt();
		th.interrupt();

		th.join();
	}

	private static void test01() {
		final TabletStore ts = new TabletStore();

		// ----

		ts.add();
		ts.get();

		// ----

		new Thread() {
			@Override
			public void run() {
				ThreadTools.sleep(500);
				ts.add();
			}
		}
		.start();

		System.out.println("*1");
		ts.get();
		System.out.println("*2");

		// ----

		new Thread() {
			@Override
			public void run() {
				ThreadTools.sleep(500);
				ts.add();
				ThreadTools.sleep(500);
				ts.add();
				ThreadTools.sleep(500);
				ts.add();
			}
		}
		.start();

		System.out.println("*1");
		ts.get();
		System.out.println("*2");
		ts.get();
		System.out.println("*3");
		ts.get();
		System.out.println("*4");

		// ----
	}
}
