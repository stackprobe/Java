package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.Logger;

public class LoggerTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static Logger _logger;

	private static void test01() throws Exception {
		//_logger = Logger.create("C:/temp/LoggerTest", "LoggerTest_");
		_logger = Logger.createForResident("C:/temp/LoggerTest", "LoggerTest_");
		try {
			test01(1);
			test01(2);
		}
		finally {
			FileTools.close(_logger);
		}
	}

	private static void test01(int prm) {
		_logger.println("開始します。");
		_logger.println("prm == " + prm);

		try {
			try {
				try {
					try {
						if(prm != 1) {
							throw new Exception("パラメータが1ではありません。prm=" + prm);
						}
					}
					catch(Throwable e) {
						throw new Exception("三段目", e);
					}
				}
				catch(Throwable e) {
					throw new Exception("二段目", e);
				}
			}
			catch(Throwable e) {
				throw new Exception("一段目", e);
			}
			_logger.println("正常終了しました。");
		}
		catch(Throwable e) {
			_logger.print(e);
			_logger.println("異常終了しました。");
		}
	}
}
