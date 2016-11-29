package charlotte_test.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.DebugTools;
import charlotte.tools.FileTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.MathTools;
import charlotte.tools.Merging;
import charlotte.tools.QueueData;
import charlotte.tools.StringTools;

public class HugeQueueTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test03();
			test04();

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

	private static void test03() throws Exception {
		for(int c = 0; c < 100; c++) {
			System.out.println("c: " + c);

			List<String> lines = DebugTools.makeRandLines(
					StringTools.ASCII + StringTools.ZEN_HIRAGANA + StringTools.ZEN_KATAKANA,
					MathTools.random(1000),
					0,
					1000
					);
			HugeQueue hq = null;

			try {
				hq = new HugeQueue();

				for(String line : lines) {
					hq.add(line);
				}
				hq.sortText();
				ArrayTools.sort(lines, StringTools.comp);

				if(ArrayTools.isSame(lines, hq.toStringList(), StringTools.comp) == false) {
					throw null; // bug
				}
			}
			finally {
				FileTools.close(hq);
			}
		}
	}

	private static void test04() throws Exception {
		for(int c = 0; c < 100; c++) {
			System.out.println("test04_c: " + c);

			List<String> lOnly = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);
			List<String> both  = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);
			List<String> rOnly = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);

			daburase(lOnly);
			daburase(both);
			daburase(rOnly);

			List<String> l = new ArrayList<String>();
			List<String> r = new ArrayList<String>();

			l.addAll(lOnly);
			l.addAll(both);

			r.addAll(rOnly);
			r.addAll(both);

			ArrayTools.shuffle(l);
			ArrayTools.shuffle(r);

			test04_2(l, r);
		}
	}

	private static void daburase(List<String> list) {
		if(MathTools.random(2) == 0) {
			int num = MathTools.random(list.size() * 3 + 1);

			for(int c = 0; c < num; c++) {
				list.add(list.get(c));
			}
		}
	}

	private static void test04_2(List<String> l, List<String> r) throws Exception {
		HugeQueue lq = null;
		HugeQueue rq = null;
		HugeQueue bq = null;
		try {
			lq = createHq(l);
			rq = createHq(r);
			bq = new HugeQueue();

			List<String> both = new ArrayList<String>();

			HugeQueue.mergeText(lq, rq, bq, null);
			Merging.mergeText(l, r, both, null);

			List<String> l2 = createList(lq);
			List<String> r2 = createList(rq);
			List<String> b2 = createList(bq);

			ArrayTools.sort(l, StringTools.comp);
			ArrayTools.sort(r, StringTools.comp);
			ArrayTools.sort(both, StringTools.comp);
			ArrayTools.sort(l2, StringTools.comp);
			ArrayTools.sort(r2, StringTools.comp);
			ArrayTools.sort(b2, StringTools.comp);

			if(ArrayTools.isSame(l2, l, StringTools.comp) == false) throw null; // bug
			if(ArrayTools.isSame(r2, r, StringTools.comp) == false) throw null; // bug
			if(ArrayTools.isSame(b2, both, StringTools.comp) == false) throw null; // bug
		}
		finally {
			FileTools.close(lq);
			FileTools.close(rq);
			FileTools.close(bq);
		}
	}

	private static HugeQueue createHq(List<String> lines) {
		HugeQueue hq = new HugeQueue();

		for(String line : lines) {
			hq.add(line);
		}
		return hq;
	}

	private static List<String> createList(HugeQueue hq) {
		List<String> lines = new ArrayList<String>();

		for(; ; ) {
			String line = hq.pollString();

			if(line == null) {
				break;
			}
			lines.add(line);
		}
		return lines;
	}
}
