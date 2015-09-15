package charlotte.satellite;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class WinAPITools {
	public static final long INFINITE = 0xffffffffL;

	private static String _winAPIToolsFile;

	private static synchronized String getWinAPIToolsFile() throws Exception {
		if(_winAPIToolsFile == null) {
			String file1 = FileTools.makeTempPath(StringTools.getUUID() + "_WinAPITools.exe");
			String file2 = FileTools.makeTempPath(StringTools.getUUID() + "_WinAPITools.exe_");
			String file3 = FileTools.combine(
					FileTools.makeTempPath("{b46c0dfc-6df3-45e3-9b78-38e3b4f2cd9b}"),
					"WinAPITools.exe"
					);
			byte[] fileData = FileTools.readToEnd(WinAPITools.class.getResource("res/WinAPITools.exe_"));

			FileTools.writeAllBytes(file1, fileData);
			FileTools.writeAllBytes(file2, fileData);

			{
				String command = "\"" + file1 + "\" /EXTRACT \"" + file2 + "\" \"" + file3 + "\" ;";
				//System.out.println(command); // test
				Runtime.getRuntime().exec(command).waitFor();
			}

			FileTools.delete(file1);
			//FileTools.delete(file2);
			_winAPIToolsFile = file3;
		}
		return _winAPIToolsFile;
	}

	private static void go(String args) {
		try {
			String command = "\"" + getWinAPIToolsFile() + "\" " + args;
			//System.out.println(command); // test
			Runtime.getRuntime().exec(command).waitFor();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void mutexWaitOne(String targetName, long millis, String beganName, String wObj0File, String endName, int parentPID) {
		go("/MUTEX-WAIT-ONE " + targetName + " " + millis + " " + beganName + " \"" + wObj0File + "\" " + endName + " " + parentPID);
	}

	public static void eventCreate(String targetName, String beganName, String endName, int parentPID) {
		go("/EVENT-CREATE " + targetName + " " + beganName + " " + endName + " " + parentPID);
	}

	public static void eventSet(String targetName) {
		go("/EVENT-SET " + targetName);
	}

	public static void eventWaitOne(String targetName, long millis, int parentPID) {
		go("/EVENT-WAIT-ONE " + targetName + " " + millis + " " + parentPID);
	}

	public static String getEnv(String name, String defval) throws Exception {
		byte[] fileData;
		String file = FileTools.makeTempPath();

		try {
			go("/GET-ENV \"" + name + "\" \"" + file + "\"");
			fileData = FileTools.readAllBytes(file);
		}
		finally {
			FileTools.delete(file);
		}
		String value = new String(fileData, StringTools.CHARSET_SJIS);

		if(StringTools.isEmpty(value)) {
			return defval;
		}
		return value;
	}

	public static void deadAndRemove(String beganName, String deadName, String mtxName, String targetPath, int parentPID) {
		go("/DEAD-AND-REMOVE " + beganName + " " + deadName + " " + mtxName + " \"" + targetPath + "\" " + parentPID);
	}

	public static void deleteDelayUntilReboot(String targetPath) {
		go("/DELETE-DELAY-UNTIL-REBOOT \"" + targetPath + "\"");
	}

	public static boolean isProcessAlive(int pid) {
		String trueFile = FileTools.makeTempPath();

		try {
			go("/CHECK-PROCESS-ALIVE " + pid + " \"" + trueFile + "\"");
			return FileTools.exists(trueFile);
		}
		finally {
			FileTools.delete(trueFile); // 無いこともある。
		}
	}
}
