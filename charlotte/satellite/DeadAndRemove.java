package charlotte.satellite;

import charlotte.tools.StringTools;

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
				WinAPITools.i().deadAndRemove(_beganName, _deadName, _mtxName, _targetPath);
			}
		};
		_th.start();

		WinAPITools.i().eventWaitOne(_beganName, WinAPITools.INFINITE);
	}

	public void dead() throws Exception {
		WinAPITools.i().eventSet(_deadName);
		_th.join();
	}
}
