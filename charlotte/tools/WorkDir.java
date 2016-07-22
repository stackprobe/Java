package charlotte.tools;

import java.io.Closeable;

public class WorkDir implements Closeable {
	public static WorkDir create() {
		final WorkDir ret = new WorkDir();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				FileTools.close(ret);
			}
		});

		return ret;
	}

	private String _ident;
	private String _dir;

	public WorkDir() {
		this(StringTools.getUUID());
	}

	public WorkDir(String ident) {
		_ident = ident;
		_dir = FileTools.makeTempPath(_ident);

		FileTools.rm(_dir);
		FileTools.mkdir(_dir);
	}

	public String getIdent() {
		return _ident;
	}

	public String getDir() {
		return _dir;
	}

	public String makeSubPath() {
		return FileTools.combine(_dir, StringTools.getUUID());
	}

	public String makeSubPath(String ident) {
		return FileTools.combine(_dir, ident);
	}

	@Override
	public void close() {
		if(_dir != null) {
			FileTools.rm(_dir);
			_dir = null;
		}
	}
}
