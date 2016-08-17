package evergarden.sudokugen;

import charlotte.tools.AutoTable;
import charlotte.tools.CsvData;
import charlotte.tools.FileTools;
import charlotte.tools.MathTools;
import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;

public class GenInput {
	private static final String SUDOKU_EXE = "C:/Dev/Main/Sudoku/Sudoku/Release/Sudoku.exe";
	private String _dir;
	private String _inputCsvFile;
	private String _outputCsvFile;

	public GenInput(String dir) {
		_dir = dir;
		_inputCsvFile = FileTools.combine(_dir, "Input.csv");
		_outputCsvFile = FileTools.combine(_dir, "Output.csv");
	}

	private AutoTable<Integer> _input;
	private int _maxNumb;

	public void perform() throws Exception {
		FileTools.createFile(_inputCsvFile);
		FileTools.del(_outputCsvFile);

		callSudokuExe();
		loadInput();

		for(; ; ) {
			//writeInputRandomSeed(0);
			writeInputRandomSeed(3);
			//writeInputRandomSeed(5);

			FileTools.del(_outputCsvFile); // 2bs
			callSudokuExe();

			if(FileTools.exists(_outputCsvFile)) {
				break;
			}
			FileTools.del(_outputCsvFile);
		}
		loadInput();

		final long TIME_LIMIT = 60000L;
		long timeLimit = System.currentTimeMillis() + TIME_LIMIT;

		for(int loopCount = 1; ; loopCount++) {
			System.out.println("loopCount: " + loopCount);

			for(int tryCount = 1; ; tryCount++) {
				int x;
				int y;

				do {
					x = MathTools.random(_input.getWidth());
					y = MathTools.random(_input.getHeight());
				}
				while(_input.get(x, y) == null);

				int origNumb = _input.get(x, y);

				boolean fault = false;

				for(int numb = 1; numb <= _maxNumb; numb++) {
					if(numb == origNumb) {
						continue;
					}
					_input.set(x, y, numb);

					writeInput();
					FileTools.del(_outputCsvFile); // 2bs
					callSudokuExe();

					fault = FileTools.exists(_outputCsvFile);

					FileTools.del(_outputCsvFile);

					if(fault) {
						break;
					}
				}
				if(fault) {
					_input.set(x, y, origNumb);
				}
				else {
					_input.set(x, y, null);
					timeLimit = System.currentTimeMillis() + TIME_LIMIT;
					break;
				}
				if(timeLimit < System.currentTimeMillis()) {
					break;
				}
			}
			if(timeLimit < System.currentTimeMillis()) {
				break;
			}
		}
		writeInput();
	}

	private void callSudokuExe() throws Exception {
		//SystemTools.execOnBatch("\"" + SUDOKU_EXE + "\" /D \"" + _dir + "\""); // データによって何故か固まる。
		SystemTools.execOnBatch("\"" + SUDOKU_EXE + "\" /D \"" + _dir + "\" > C:/temp/1.txt"); // これならok...

		// これもダメ...
		/*
		Process r = Runtime.getRuntime().exec("\"" + SUDOKU_EXE + "\" /D \"" + _dir + "\" > C:/temp/1.txt");
		Thread.sleep(100);
		r.waitFor();
		*/

		/*
		//String commandLine = SUDOKU_EXE + " /D " + _dir;
		String commandLine = "\"" + SUDOKU_EXE + "\" /D \"" + _dir + "\"";

		String file = FileTools.makeTempPath() + ".bat";

		//System.out.println("Go " + commandLine);
		Runtime.getRuntime().exec(commandLine).waitFor();
		//System.out.println("End");
		*/
	}

	private void loadInput() throws Exception {
		CsvData csv = new CsvData();
		csv.readFile(_outputCsvFile);
		AutoTable<String> table = csv.getTable();

		_input = new AutoTable<Integer>();
		_maxNumb = 0;

		for(int x = 0; x < table.getWidth(); x++) {
			for(int y = 0; y < table.getHeight(); y++) {
				String cell = table.get(x, y);

				if(StringTools.isEmpty(cell) == false) {
					int cellInt = Integer.parseInt(cell);

					_input.set(x, y, cellInt);
					_maxNumb = Math.max(_maxNumb, cellInt);
				}
			}
		}
	}

	private void writeInput() throws Exception {
		AutoTable<String> table = new AutoTable<String>();

		for(int x = 0; x < _input.getWidth(); x++) {
			for(int y = 0; y < _input.getHeight(); y++) {
				if(_input.get(x, y) != null) {
					table.set(x, y, "" + _input.get(x, y));
				}
			}
		}
		CsvData csv = new CsvData();
		csv.setTable(table);
		csv.writeFile(_inputCsvFile);
	}

	private void writeInputRandomSeed(int num) throws Exception {
		AutoTable<String> table = new AutoTable<String>();

		for(int c = 0; c < num; c++) {
			int x;
			int y;

			do {
				x = MathTools.random(_input.getWidth());
				y = MathTools.random(_input.getHeight());
			}
			while(_input.get(x, y) == null);

			table.set(x, y, "" + (MathTools.random(_maxNumb) + 1));
		}
		CsvData csv = new CsvData();
		csv.setTable(table);
		csv.writeFile(_inputCsvFile);
	}
}
