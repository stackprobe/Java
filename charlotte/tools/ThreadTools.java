package charlotte.tools;

public class ThreadTools {
	public static void join(Thread th) {
		try {
			th.join();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
