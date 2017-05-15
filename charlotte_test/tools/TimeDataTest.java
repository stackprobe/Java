package charlotte_test.tools;

import charlotte.tools.TimeData;

public class TimeDataTest {
	public static void main(String[] args) {
		try {
			System.out.println("now: " + TimeData.now());

			System.out.println("2004-04-01T12:00:00+09:00: " + TimeData.fromISO8061("2004-04-01T12:00:00+09:00"));

			test01("00010101000000");
			test01("19990715131515");
			test01("20040401090000");

			System.out.println("" + TimeData.fromString("20000101000000").getEpochTime());
			System.out.println("" + TimeData.fromString("30000101000000").getEpochTime());

			System.out.println("" + TimeData.fromEpochTime(1000000000L).getString());
			System.out.println("" + TimeData.fromEpochTime(32500000000L).getString());
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01(String src) throws Exception {
		System.out.println("src: " + src);

		TimeData td = TimeData.fromString(src);
		String ret = td.getString();

		System.out.println("ret: " + ret);

		if(src.equals(ret) == false) {
			throw new Exception("ng");
		}
	}
}
