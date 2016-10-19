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

			System.out.println("[" + StringTools.trim("　　　") + "]");
			System.out.println("[" + StringTools.trim("　　　$t　　　") + "]");
			System.out.println("[" + StringTools.trim("　　　\t　　　") + "]");
			System.out.println("[" + StringTools.trim("　\t　\t　$$　\t　\t　") + "]");

			for(String a : ":+:-".split("[:]")) {
				for(String b : "0:1:12:123:1234".split("[:]")) {
					for(String c : ":.0:.01:.012:.0123:.01234".split("[:]")) {
						String str = a + b + c;

						System.out.println(str + " -> " + StringTools.thousandComma(str));
					}
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
