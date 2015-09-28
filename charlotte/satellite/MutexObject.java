package charlotte.satellite;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.ThreadTools;

public class MutexObject {
	private String _targetName;
	private String _beganName;
	private String _wObj0File;
	private String _endName;
	private Thread _th;

	/**
	 *
	 * @param name 空白を含まないこと、コマンドラインに使用できる文字列であること。
	 */
	public MutexObject(String name) {
		_targetName = name;
	}

	public boolean waitOne(final long millis) {
		//System.out.println("W"); // test
		//new Exception().printStackTrace(); // test
		//System.out.println("W_2"); // test

		_beganName = StringTools.getUUID();
		_wObj0File = FileTools.makeTempPath();
		_endName = StringTools.getUUID();

		_th = new Thread() {
			@Override
			public void run() {
				WinAPITools.mutexWaitOne(_targetName, millis, _beganName, _wObj0File, _endName);
			}
		};
		_th.start();

		WinAPITools.eventWaitOne(_beganName, WinAPITools.INFINITE);

		if(FileTools.exists(_wObj0File)) {
			FileTools.delete(_wObj0File);
			//System.out.println("G"); // test
			return true;
		}
		release();
		//System.out.println("X"); // test
		return false;
	}

	public void release() {
		//System.out.println("R"); // test

		WinAPITools.eventSet(_endName);
		ThreadTools.join(_th);
		_th = null;
	}

	public boolean isLocking() {
		return _th != null;
	}
}
