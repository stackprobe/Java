package charlotte_test.tools;

import charlotte.tools.IndentedText;
import charlotte.tools.StringTools;
import charlotte.tools.WktParser;

public class IndentedTextTest {
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
		IndentedText.WktBuffer wb = new IndentedText.WktBuffer();

		wb.enter("ROOT");
		wb.enter("UNDER_OF_ROOT");

		for(int c = 0; c < 3; c++) {
			wb.enter("NODE");
			wb.add("INDEX(" + c + ")");
			wb.add("TEXT(\"" + WktParser.encodeString(StringTools.random(StringTools.ASCII, 0, 10)) + "\")");

			for(int d = 0; d < 3; d++) {
				wb.enter("LEAF");
				wb.add("INDEX(" + d + ")");
				wb.add("TEXT(\"" + WktParser.encodeString(StringTools.random(StringTools.ASCII, 0, 10)) + "\")");
				wb.leave();
			}
			wb.leave();
		}
		wb.leave();
		wb.leave();

		System.out.println(wb.getText());
	}
}
