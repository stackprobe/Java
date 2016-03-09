package charlotte_test.tools;

import charlotte.tools.CsvData;

public class CsvDataTest {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		CsvData csv = new CsvData();

		csv.readFile("C:/tmp/test.csv");
		csv.ttr();
		csv.writeFile("C:/temp/test.csv");

		csv.readFile("C:/tmp/test2.csv");
		csv.tt();
		csv.writeFile("C:/temp/test2.csv");
	}
}
