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
		CsvData cd = new CsvData();

		cd.readFile("C:/tmp/test.csv");
		cd.ttr();
		cd.writeFile("C:/temp/test.csv");

		cd.readFile("C:/tmp/test2.csv");
		cd.tt();
		cd.writeFile("C:/temp/test2.csv");
	}
}
