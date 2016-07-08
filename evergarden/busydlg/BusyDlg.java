package evergarden.busydlg;

import java.io.Closeable;

import charlotte.tools.FileTools;
import charlotte.tools.SystemTools;
import charlotte.tools.WorkDir;

public class BusyDlg implements Closeable {
	private WorkDir _dir;
	private String _file;
	private Thread _th;

	public BusyDlg(final String message) {
		try {
			_dir = new WorkDir();
			_file = _dir.makeSubPath("BusyDlg.exe");

			FileTools.writeAllBytes(
					_file,
					FileTools.readToEnd(BusyDlg.class.getResource("res/BusyDlg.exe_"))
					);

			_th = new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(
								"\"" + _file + "\" " + _dir.getIdent() + " 0 " + SystemTools.PID + " \"" + message + "\""
								)
								.waitFor();
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};
			_th.start();
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}

	@Override
	public void close() {
		try {
			if(_th != null) {
				{
					int millis = 50;

					do {
						Runtime.getRuntime().exec("\"" + _file + "\" " + _dir.getIdent() + " 1 -1 \"\"").waitFor();

						if(millis < 200) {
							millis++;
						}
						Thread.sleep(millis);
					}
					while(_th.isAlive());
				}

				// 2bs
				{
					int millis = 0;

					do {
						FileTools.del(_file);

						if(millis < 200) {
							millis++;
						}
						Thread.sleep(millis);
					}
					while(FileTools.exists(_file));
				}

				_dir.close();
				_dir = null;
				_file = null;
				_th = null;
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}
}
