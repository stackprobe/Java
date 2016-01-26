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

			System.out.println(StringTools.replaceEnclosed(
					"<html><head></head><body>abc</body></html>",
					"<body>",
					"<",
					"123"
					));

			if(StringTools.startsWith("ABC123", "ABC") == false ||
					StringTools.startsWith("ABC123", "abc") ||
					StringTools.startsWithIgnoreCase("ABC123", "ABC") == false ||
					StringTools.startsWithIgnoreCase("ABC123", "abc") == false
					) {
				throw new Exception();
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
