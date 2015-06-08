package charlotte.satellite;

import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;

public class DeadAndRemove {
	private String _beganName;
	private String _deadName;
	private String _mtxName;
	private String _targetPath;
	private Thread _th;

	public DeadAndRemove(String mtxName, String targetPath) {
		_beganName = StringTools.getUUID();
		_deadName = StringTools.getUUID();
		_mtxName = mtxName;
		_targetPath = targetPath;

		_th = new Thread(){
			@Override
			public void run() {
				WinAPITools.deadAndRemove(_beganName, _deadName, _mtxName, _targetPath, SystemTools.PID);
			}
		};
		_th.start();

		WinAPITools.eventWaitOne(_beganName, WinAPITools.INFINITE, SystemTools.PID);
	}

	public void dead() throws Exception {
		WinAPITools.eventSet(_deadName);
		_th.join();
	}
}
