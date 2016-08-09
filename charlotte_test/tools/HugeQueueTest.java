package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.HugeQueue;

public class HugeQueueTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		HugeQueue hq = null;

		try {
			hq = new HugeQueue();

			// ----

			hq.add("a");
			hq.add("bb");
			hq.add("ccc");

			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());

			// ----

			hq.add("a");
			hq.add("bb");
			hq.add("ccc");
			hq.add("dddd");
			hq.add("eeeee");

			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());

			hq.add("ffffff");
			hq.add("ggggggg");

			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());
			System.out.println(hq.pollString());

			// ----
		}
		finally {
			FileTools.close(hq);
		}
	}
}
