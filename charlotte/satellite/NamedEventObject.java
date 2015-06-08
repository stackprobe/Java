package charlotte.satellite;

import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;
import charlotte.tools.ThreadTools;

public class NamedEventObject {
	private String _targetName;
	private String _beganName;
	private String _endName;
	private Thread _th;

	/**
	 *
	 * @param name 空白を含まないこと、コマンドラインに使用できる文字列であること。
	 */
	public NamedEventObject(String name) {
		_targetName = name;
		_beganName = StringTools.getUUID();
		_endName = StringTools.getUUID();

		_th = new Thread() {
			@Override
			public void run() {
				WinAPITools.eventCreate(_targetName, _beganName, _endName, SystemTools.PID);
			}
		};
		_th.start();

		WinAPITools.eventWaitOne(_beganName, WinAPITools.INFINITE, SystemTools.PID);
	}

	public void set() {
		WinAPITools.eventSet(_targetName);
	}

	public void waitOne(long millis) {
		WinAPITools.eventWaitOne(_targetName, millis, SystemTools.PID);
	}

	public void close() {
		WinAPITools.eventSet(_endName);
		ThreadTools.join(_th);
	}
}
