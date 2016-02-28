package charlotte.satellite;

import charlotte.tools.FileTools;
import charlotte.tools.QueueData;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;

public class WinAPITools {
	public static final long INFINITE = 0xffffffffL;

	private static WinAPITools _i;

	public static synchronized WinAPITools i() {
		if(_i == null) {
			_i = new WinAPITools();
		}
		return _i;
	}

	private static final String WIN_API_TOOLS_FILE_ID = "{b46c0dfc-6df3-45e3-9b78-38e3b4f2cd9b}"; // shared_uuid
	private String _winAPIToolsFile;
	private String _monitorName;

	private WinAPITools() {
		try {
			String tmpId = StringTools.getUUID();
			String file1 = FileTools.makeTempPath(tmpId + "_WinAPITools.exe");
			String file2 = FileTools.makeTempPath(tmpId + "_WinAPITools.exe_");
			byte[] fileData = FileTools.readToEnd(WinAPITools.class.getResource("res/WinAPITools.exe_"));
			String fileHash = SecurityTools.getSHA512_128String(fileData);
			String file3 = FileTools.makeTempPath(WIN_API_TOOLS_FILE_ID + "/" + fileHash + "/WinAPITools.exe");
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
			_monitorName = StringTools.getUUID();

			{
				String command = "\"" + _winAPIToolsFile + "\" /MONITOR " + SystemTools.PID + " " + _monitorName;
				//System.out.println(command); // test
				Runtime.getRuntime().exec(command);
			}

			{
				String trueFile = FileTools.makeTempPath();
				int millis = 1;

				for(; ; ) {
					{
						String command = "\"" + _winAPIToolsFile + "\" /CHECK-MUTEX-LOCKED " + _monitorName + " \"" + trueFile + "\"";
						//System.out.println(command); // test
						Runtime.getRuntime().exec(command).waitFor();
					}

					if(FileTools.exists(trueFile)) {
						break;
					}
					Thread.sleep(millis);
					millis = Math.min(millis + 1, 2000);
				}
				FileTools.delete(trueFile);
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private void go(String args) {
		try {
			String command = "\"" + _winAPIToolsFile + "\" " + args;
			//System.out.println(command); // test
			Runtime.getRuntime().exec(command).waitFor();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public void mutexWaitOne(String targetName, long millis, String beganName, String wObj0File, String endName) {
		go("/MUTEX-WAIT-ONE " + targetName + " " + millis + " " + beganName + " \"" + wObj0File + "\" " + endName + " " + _monitorName);
	}

	public void eventCreate(String targetName, String beganName, String endName) {
		go("/EVENT-CREATE " + targetName + " " + beganName + " " + endName + " " + _monitorName);
	}

	public void eventSet(String targetName) {
		go("/EVENT-SET " + targetName);
	}

	public void eventWaitOne(String targetName, long millis) {
		go("/EVENT-WAIT-ONE " + targetName + " " + millis + " " + _monitorName);
	}

	public String getEnv(String name, String defval) throws Exception {
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

	public void deadAndRemove(String beganName, String deadName, String mtxName, String targetPath) {
		go("/DEAD-AND-REMOVE " + beganName + " " + deadName + " " + mtxName + " \"" + targetPath + "\" " + _monitorName);
	}

	public void deleteDelayUntilReboot(String targetPath) {
		go("/DELETE-DELAY-UNTIL-REBOOT \"" + targetPath + "\"");
	}

	public boolean isProcessAlive(int pid) {
		String trueFile = FileTools.makeTempPath();

		try {
			go("/CHECK-PROCESS-ALIVE " + pid + " \"" + trueFile + "\"");
			return FileTools.exists(trueFile);
		}
		finally {
			FileTools.delete(trueFile); // 無いこともある。
		}
	}

	public void sendToFortewave(String identHash, QueueData<byte[]> sq) throws Exception {
		String dir = FileTools.makeTempPath();
		FileTools.mkdir(dir);

		for(int index = 0; 1 <= sq.size(); index++) {
			FileTools.writeAllBytes(
					FileTools.combine(dir, StringTools.zPad(index, 4)),
					sq.poll()
					);
		}
		go("/SEND-TO-FORTEWAVE " + identHash + " \"" + dir + "\"");
	}

	public void recvFromFortewave(String identHash, QueueData<byte[]> rq, long millis, int recvLimit) throws Exception {
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
	}
}
