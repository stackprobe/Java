package charlotte.tools;

public class ThreadTools {
	public static void interrupt(Thread th) {
		try {
			th.interrupt();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void join(Thread th) {
		try {
			th.join();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
