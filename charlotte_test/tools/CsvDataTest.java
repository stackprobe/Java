package charlotte_test.tools;

import charlotte.tools.CsvData;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class CsvDataTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			test03();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		CsvData csv = new CsvData();

		csv.readFile("C:/tmp/test.csv");
		csv.ttr();
		csv.writeFile("C:/temp/test.csv");

		csv.readFile("C:/tmp/test2.csv");
		csv.tt();
		csv.writeFile("C:/temp/test2.csv");
	}

	private static void test02() throws Exception {
		CsvData.Stream reader = new CsvData.Stream("C:/tmp/ユーザーレイヤ.csv");
		CsvData.Stream writer = new CsvData.Stream("C:/temp/1.csv");
		try {
			reader.readOpen();
			writer.writeOpen();

			writer.writeRows(reader.readToEnd());
		}
		finally {
			reader.readClose();
			writer.writeClose();
		}

		// - - -

		writer = new CsvData.Stream("C:/temp/2.csv");
		try {
			writer.writeOpen();

			writer.add("#");
			writer.endRow();

			writer.add("1");
			writer.add("2");
			writer.add("3");
			writer.endRow();

			writer.add("a");
			writer.add("b");
			writer.add("c");
			writer.add("d");
			writer.add("e");
			writer.endRow();

			writer.add("");
			writer.add("");
			writer.add("");
			writer.endRow();

			writer.endRow();
			writer.endRow();
			writer.endRow();

			writer.add("/");
			writer.endRow();

			writer.add("漢字");
			writer.add("\"dblQuote\"");
			writer.add("1行目\n2行目\n3行目");
			writer.endRow();
		}
		finally {
			writer.writeClose();
		}
	}

	private static void test03() throws Exception {
		for(String file : FileTools.ls("C:/var/テスト用csv")) {
			if(StringTools.endsWithIgnoreCase(file, ".csv")) {
				test03(file);
			}
		}
	}

	private static void test03(String file) throws Exception {
		System.out.println("file: " + file); // test

		CsvData.Stream reader = new CsvData.Stream(file);
		CsvData.Stream writer = new CsvData.Stream("C:/temp/1.csv");
		try {
			reader.readOpen();
			writer.writeOpen();

			writer.writeRows(reader.readToEnd());
		}
		finally {
			reader.readClose();
			writer.writeClose();
		}
		if(FileTools.isSameFile(file, "C:/temp/1.csv") == false) {
			System.out.println("not match");

			// TODO 最後に改行があるかないかの違いだけのはず!!!
		}
	}
}
