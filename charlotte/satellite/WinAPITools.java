package charlotte.satellite;

import charlotte.tools.FileTools;
import charlotte.tools.QueueData;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class WinAPITools {
	public static final long INFINITE = 0xffffffffL;

	private static String _winAPIToolsFile;

	private static synchronized String getWinAPIToolsFile() throws Exception {
		if(_winAPIToolsFile == null) {
			String file1 = FileTools.makeTempPath(StringTools.getUUID() + "_WinAPITools.exe");
			String file2 = FileTools.makeTempPath(StringTools.getUUID() + "_WinAPITools.exe_");
			byte[] fileData = FileTools.readToEnd(WinAPITools.class.getResource("res/WinAPITools.exe_"));
			String fileHash = SecurityTools.getSHA512_128String(fileData);
			String file3 = FileTools.combine(
					FileTools.combine(
							FileTools.makeTempPath("{b46c0dfc-6df3-45e3-9b78-38e3b4f2cd9b}"),
							fileHash
							),
					"WinAPITools.exe"
					);

			FileTools.writeAllBytes(file1, fileData);
			FileTools.writeAllBytes(file2, fileData);

			{
				String command = "\"" + file1 + "\" /EXTRACT \"" + file2 + "\" \"" + file3 + "\"";
				//System.out.println(command); // test
				Runtime.getRuntime().exec(command).waitFor();
			}

			FileTools.delete(file1);
			FileTools.delete(file2); // 無いこともある。
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

	public static void sendToFortewave(String identHash, QueueData<byte[]> sq) throws Exception {
		String dir = FileTools.makeTempPath();
		FileTools.mkdir(dir);

		System.out.println("stf: " + sq.size()); // test

		for(int index = 0; 1 <= sq.size(); index++) {
			FileTools.writeAllBytes(
					FileTools.combine(dir, StringTools.zPad(index, 4)),
					sq.poll()
					);
		}
		go("/SEND-TO-FORTEWAVE " + identHash + " \"" + dir + "\"");
	}

	public static void recvFromFortewave(String identHash, QueueData<byte[]> rq, long millis, int recvLimit) throws Exception {
		String dir = FileTools.makeTempPath();
		FileTools.mkdir(dir);
		go("/RECV-FROM-FORTEWAVE " + identHash + " \"" + dir + "\" " + millis + " " + recvLimit);

		for(int index = 0; ; index++) {
			String file = FileTools.combine(dir, StringTools.zPad(index, 4));

			if(FileTools.exists(file) == false) {
				break;
			}
			rq.add(FileTools.readAllBytes(file));
			FileTools.delete(file);
		}
		FileTools.delete(dir);

		System.out.println("rff: " + rq.size()); // test
	}
}
