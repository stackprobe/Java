package charlotte.tools;

public class SharedFile {
	private String _file;
	private LockingFile _lockingFile;

	public SharedFile(String file) {
		this(file, file + ".locking");
	}

	public SharedFile(String file, String lockingFile) {
		_file = file;
		_lockingFile = new LockingFile(lockingFile);
	}

	public byte[] read() throws Exception {
		_lockingFile.open();
		return FileTools.readAllBytes(_file);
	}

	public void write(byte[] fileData) throws Exception {
		FileTools.writeAllBytes(_file, fileData);
		this.release();
	}

	public void release() {
		_lockingFile.close();
	}
}
