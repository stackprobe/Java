package charlotte.tools;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class LockingFile {
	private String _file;

	public LockingFile(String file) {
		_file = file;
	}

	public boolean open() {
		return open(20, 100);
	}

	public boolean open(int tryCountMax, long baseWaitMillis) {
		for(int count = 1; count <= tryCountMax; count++) {
			if(tryOpen()) {
				return true;
			}
			try {
				Thread.sleep(baseWaitMillis * count + SystemTools.random(200));
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private FileOutputStream _fos; // FileInputStream じゃダメだった。
	private FileChannel _fc;
	private FileLock _fl;

	/**
	 * ロックする。
	 * @return ? ロックに成功した || 既にロックしている
	 */
	private boolean tryOpen() {
		try {
			if(_fos != null) {
				return true; // already locking
			}
			_fos = new FileOutputStream(_file);
			_fc = _fos.getChannel();
			_fl = _fc.tryLock();

			if(_fl == null) {
				throw new Exception("ロック失敗");
			}
			return true;
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
			this.close();
		}
		return false;
	}

	/**
	 * ロック解除する。
	 * 既にロック解除されている場合は、何もしない。
	 */
	public void close() {
		try {
			_fl.release();
		}
		catch(Throwable e) {
			// ignore
		}
		FileTools.close(_fos);
		_fos = null;
		_fc = null;
		_fl = null;
	}
}
