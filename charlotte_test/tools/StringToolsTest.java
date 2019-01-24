package charlotte_test.tools;

import charlotte.tools.StringTools;
import charlotte.tools.StringTools.RealNumber;

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

			testRn("0");
			testRn("1");
			testRn("10");
			testRn("-1");
			testRn("-10");
			testRn("0.1");
			testRn("1.0");
			testRn("-0.1");
			testRn("-1.0");
			testRn("123.456789");
			testRn("123456.789");
			testRn("123456789.000");
			testRn("000123456789");
			testRn("0.000123456789");
			testRn("0000.123456789");
			testRn("0000123.456789");
			testRn("0000123456.789");
			testRn("0.000123456789000");
			testRn("0000.123456789000");
			testRn("0000123.456789000");
			testRn("0000123456.789000");
			testRn("0000123456789.000");
			testRn("-0000123456789.000");

			System.out.println(mmToM("123456000"));
			System.out.println(mmToM("123456"));
			System.out.println(mmToM("123.456"));
			System.out.println(mmToM("12.3456"));
			System.out.println(mmToM("1.23456"));
			System.out.println(mmToM("0.123456"));
			System.out.println(mmToM("0.000123456"));
			System.out.println(mmToM("0"));
			System.out.println(mmToM(""));
			System.out.println(mmToM(null));
			System.out.println(mmToM("あいう"));
			System.out.println(mmToM("あいう012"));
			System.out.println(mmToM("あいう012えお000"));
			System.out.println(mmToM("あいう012えお000かき000"));
			System.out.println(mmToM("あいう012えお000かき123"));

			System.out.println(StringTools.replaceIgnoreCase("Id = '${id}'", "${ID}", "123"));
			System.out.println(StringTools.replaceIgnoreCase("abc", "ABC", "123"));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void testRn(String str) {
		System.out.println("< " + str);

		StringTools.RealNumber rn = new StringTools.RealNumber(str);

		System.out.println("> " + rn);
		System.out.println("rn.value: [" + rn.value + "]");
		System.out.println("rn.exponent: " + rn.exponent);
		System.out.println("rn.sign: " + rn.sign);

		if(str.equals(rn.toString()) == false) {
			throw null;
		}
		rn.trim();
		System.out.println("T> " + rn);
	}

	private static String mmToM(String str) {
		if(StringTools.isEmpty(str)) {
			return str;
		}
		RealNumber rn = new RealNumber(str);

		if(rn.value.equals("")) {
			return str;
		}
		rn.fix();
		rn.trim();
		rn = new RealNumber(rn.toString());
		rn.exponent -= 3;
		return rn.toString();
	}
}
