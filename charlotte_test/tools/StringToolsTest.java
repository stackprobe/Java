package charlotte_test.tools;

import charlotte.tools.StringTools;

public class StringToolsTest {
	public static void main(String[] args) {
		try {
			System.out.println(StringTools.getUUID());
			System.out.println(StringTools.getUUID());
			System.out.println(StringTools.getUUID());

			System.out.println(StringTools.set("ABC", 0, 'X'));
			System.out.println(StringTools.set("ABC", 1, 'X'));
			System.out.println(StringTools.set("ABC", 2, 'X'));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
