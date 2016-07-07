package charlotte_test.tools;

import java.util.List;

import charlotte.tools.CsvFileSorter;
import charlotte.tools.DebugTools;
import charlotte.tools.StringTools;
import charlotte.tools.TextFileSorter;

public class FileSorterTest {
	public static void main(String[] args) {
		try {
			test01();
			//test02();
			//test03();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		test01(0, 0);

		for(int linecnt = 1; linecnt <= 10; linecnt++) {
			for(int chrcnt = 1; chrcnt <= 10; chrcnt++) {
				test01(linecnt, chrcnt);
			}
		}

		test01(  1, 100);
		test01( 10, 100);
		test01(100, 100);

		test01(100,   1);
		test01(100,  10);
		test01(100, 100);

		test01(1000, 1);
		test01(10000, 1);
		test01(100000, 1);
		test01(1000000, 1);
		test01(3000000, 1);
		test01(10000000, 1);
		test01(30000000, 1);

		test01(1000000, 2);
		test01(3000000, 2);
		test01(1000000, 3);
		test01(3000000, 3);

		test01(1000, 30);
		test01(10000, 30);
		test01(100000, 30);
		test01(1000000, 30);

		test01(1000, 1000);
		test01(10000, 1000);
		test01(100000, 1000);
		test01(1000000, 1000);
	}

	private static void test01(int linecnt, int chrcnt) throws Exception {
		System.out.println("begin: " + linecnt + ", " + chrcnt);

		System.out.println("begin make random text file");
		DebugTools.makeRandTextFile(
				"C:/temp/1.txt",
				StringTools.CHARSET_SJIS,
				StringTools.ASCII,
				"\r\n",
				linecnt,
				0,
				chrcnt
				);

		System.out.println("begin copy");
		DebugTools.copyFile(
				"C:/temp/1.txt",
				"C:/temp/2.txt"
				);

		System.out.println("begin sort 1/2");
		Runtime.getRuntime().exec("C:/Factory/Tools/TextSort.exe C:/temp/2.txt C:/temp/3.txt").waitFor();

		System.out.println("begin sort 2/2");
		new TextFileSorter(StringTools.CHARSET_SJIS).mergeSort("C:/temp/1.txt");

		System.out.println("begin comp");
		boolean ret = DebugTools.isSameFile(
				"C:/temp/1.txt",
				"C:/temp/3.txt"
				);

		if(ret == false) {
			throw new Exception("ng");
		}
		System.out.println("done");
	}

	private static void test02() throws Exception {
		test02(1000000, 1);
		test02(1000000, 2);
		test02(1000000, 3);
		//test02(100000, 1000);
		//test02(200000, 1000);
		//test02(300000, 1000);
	}

	private static void test02(int linecnt, int chrcnt) throws Exception {
		System.out.println("begin: " + linecnt + ", " + chrcnt);

		DebugTools.makeRandTextFile(
				"C:/temp/1.txt",
				StringTools.CHARSET_SJIS,
				StringTools.ASCII,
				"\r\n",
				linecnt,
				0,
				chrcnt
				);

		long startedTime = System.currentTimeMillis();
		new TextFileSorter(StringTools.CHARSET_SJIS).mergeSort("C:/temp/1.txt");
		long endedTime = System.currentTimeMillis();

		System.out.println("done: " + (endedTime - startedTime));

		//System.out.println("elapsed: " + FileSorter.elapsed);
		//FileSorter.elapsed = 0L;
	}

	private static void test03() throws Exception {
		new CsvFileSorter(StringTools.CHARSET_SJIS) {
			@Override
			protected int comp(List<String> a, List<String> b) {
				return comp(a, b, 1);
			}
		}
		.mergeSort("C:/var/201408.csv", "C:/temp/1.csv");
	}
}
