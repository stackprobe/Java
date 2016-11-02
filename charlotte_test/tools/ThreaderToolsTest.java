package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.Threader;
import charlotte.tools.ThreaderTools;
import charlotte.tools.ValueSetter;

public class ThreaderToolsTest {
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

	private static Threader<String> _th;

	private static void test01() {
		_th = ThreaderTools.createQueue(new ValueSetter<String>() {
			@Override
			public void set(String value) {
				System.out.println("> " + value);
			}
		});

		test01add("aaa");
		test01add("bbb");
		test01add("ccc");
		test01add("1");
		test01add("2");
		test01add("ccc");
		test01add("bbb");
		test01add("aaa");
		test01add("3");
		test01add("1");
		test01add("2");
		test01add("3");

		FileTools.close(_th);
		_th = null;

		// ----

		System.out.println("createStringSet");

		_th = ThreaderTools.createStringSet(new ValueSetter<String>() {
			@Override
			public void set(String value) {
				System.out.println("> " + value);
			}
		});

		test01add("aaa");
		test01add("bbb");
		test01add("ccc");
		test01add("1");
		test01add("2");
		test01add("ccc");
		test01add("bbb");
		test01add("aaa");
		test01add("3");
		test01add("1");
		test01add("2");
		test01add("3");

		FileTools.close(_th);
		_th = null;
	}

	private static void test01add(String value) {
		System.out.println("< " + value);
		_th.add(value);
	}
}
