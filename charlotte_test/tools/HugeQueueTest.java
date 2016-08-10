package charlotte_test.tools;

import charlotte.tools.DebugTools;
import charlotte.tools.FileTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.MathTools;
import charlotte.tools.QueueData;
import charlotte.tools.StringTools;

public class HugeQueueTest {
	public static void main(String[] args) {
		try {
			test01();
			test02();

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

	private static void test02() {
		HugeQueue hq = null;
		QueueData<String> qd = new QueueData<String>();

		try {
			hq = new HugeQueue();

			for(int c = 0; c < 10000; c++) {
				if(MathTools.random(2) == 0) {
					String str = DebugTools.makeRandString(StringTools.ASCII, MathTools.random(100));

					System.out.println("< " + str);

					hq.add(str);
					qd.add(str);
				}
				else {
					String s1 = hq.pollString();
					String s2 = qd.poll();

					System.out.println(">1 " + s1);
					System.out.println(">2 " + s2);

					if(StringTools.isSame(s1, s2) == false) {
						throw null; // ng
					}
				}
				if(hq.size() != (long)qd.size()) {
					throw null; // ng
				}
			}
			for(; ; ) {
				String s1 = hq.pollString();
				String s2 = qd.poll();

				System.out.println(">1 " + s1);
				System.out.println(">2 " + s2);

				if(StringTools.isSame(s1, s2) == false) {
					throw null; // ng
				}
				if(hq.size() != (long)qd.size()) {
					throw null; // ng
				}
				if(s1 == null) {
					break;
				}
			}
		}
		finally {
			FileTools.close(hq);
		}
	}
}
