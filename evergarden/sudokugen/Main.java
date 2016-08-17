package evergarden.sudokugen;

import charlotte.tools.FileTools;

public class Main {
	public static void main(String[] args) {
		try {
			new Main().main2(args);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	//private static final String ROOT_DIR = "C:/Dev/Main/Sudoku/SampleData/自作";
	private static final String ROOT_DIR = "C:/tmp/Sudoku_SampleData_自作";

	private void main2(String[] args) throws Exception {
		//new GenInput(FileTools.combine(ROOT_DIR, "0001")).perform();
		//new GenInput(FileTools.combine(ROOT_DIR, "0002")).perform();
		new GenInput(FileTools.combine(ROOT_DIR, "0003")).perform();
		//new GenInput(FileTools.combine(ROOT_DIR, "0004")).perform()
		//new GenInput(FileTools.combine(ROOT_DIR, "0005")).perform();;
	}
}
