package charlotte_test.tools;

import charlotte.tools.FileSorter;
import charlotte.tools.StringTools;

public class FileSorterTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		// TODO

		FileSorter.TextFileSorter tfs = new FileSorter.TextFileSorter(StringTools.CHARSET_SJIS);

		tfs.mergeSort("C:/temp/1.txt");
	}
}
