package charlotte.satellite;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class WinAPITools {
	public static final long INFINITE = 0xffffffffL;

	/**
	 * WinAPITools.exe は http://stackprobe.dip.jp/_kit/Factory から取得して下さい。
	 * 必要なファイルは <Factory.zip>/Satellite/extern/WinAPITools.exe だけです。
	 * Factory.zip から WinAPITools.exe を取り出して任意の場所に置き、
	 * そこへのフルパスを返すようにこのメソッドを変更して下さい。
	 * @return WinAPITools.exe へのフルパス
	 */
	private static String getWinAPIToolsFile() {
		return System.getProperty("WIN_API_TOOLS_FILE", "C:/Factory/Satellite/extern/WinAPITools.exe");
	}

	public static boolean existWinAPIToolsFile() {
		return FileTools.exists(getWinAPIToolsFile());
	}

	private static void go(String args) {
		try {
			String command = "\"" + getWinAPIToolsFile() + "\" " + args;
			//System.out.println("command: " + command); // test
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
