package charlotte_test.satellite;

import charlotte.satellite.WinAPITools;
import charlotte.tools.FileTools;
import charlotte.tools.SystemTools;

public class WinAPIToolsTest {
	public static void main(String[] args) {
		try {
			FileTools.writeAllBytes("C:/temp/DeleteTest.dat", new byte[]{});
			WinAPITools.i().deleteDelayUntilReboot("C:/temp/DeleteTest.dat");

			System.out.println("MY_PROC: " + WinAPITools.i().isProcessAlive(SystemTools.PID));
			System.out.println("123: " + WinAPITools.i().isProcessAlive(123));
			System.out.println("456: " + WinAPITools.i().isProcessAlive(456));
			System.out.println("789: " + WinAPITools.i().isProcessAlive(789));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
