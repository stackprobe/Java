package charlotte.tools;

import java.io.Closeable;

public class WorkDir implements Closeable {
	private String _ident;
	private String _dir;

	public WorkDir() {
		_ident = StringTools.getUUID();
		_dir = FileTools.makeTempPath(_ident);

		FileTools.mkdirs(_dir); // XXX 排他
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
