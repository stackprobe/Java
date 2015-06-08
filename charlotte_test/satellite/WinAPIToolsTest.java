package charlotte_test.satellite;

import charlotte.satellite.WinAPITools;
import charlotte.tools.FileTools;
import charlotte.tools.SystemTools;

public class WinAPIToolsTest {
	public static void main(String[] args) {
		try {
			FileTools.writeAllBytes("C:/temp/DeleteTest.dat", new byte[]{});
			WinAPITools.deleteDelayUntilReboot("C:/temp/DeleteTest.dat");

			System.out.println("MY_PROC: " + WinAPITools.isProcessAlive(SystemTools.PID));
			System.out.println("123: " + WinAPITools.isProcessAlive(123));
			System.out.println("456: " + WinAPITools.isProcessAlive(456));
			System.out.println("789: " + WinAPITools.isProcessAlive(789));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
