package evergarden.violet;

public class Test03 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		final Thread th1 = new Thread() {
			public void run() {
				System.out.println("th1_1");
				try {
					Thread.sleep(Long.MAX_VALUE);
				}
				catch(Throwable e) {
					e.printStackTrace(System.out);
				}
				System.out.println("th1_2");
			}
		};

		Thread th2 = new Thread() {
			public void run() {
				System.out.println("th2_1");
				th1.interrupt();
				System.out.println("th2_2");
			}
		};

		th1.start();
		Thread.sleep(100);
		th2.start();
	}
}
