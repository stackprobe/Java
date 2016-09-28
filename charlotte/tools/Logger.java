package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Closeable {
	public static Logger create(String dir, String prefix) throws Exception {
		return create(getFileBase(dir, prefix));
	}

	private static String getFileBase(String dir, String prefix) throws Exception {
		dir = FileTools.getFullPath(dir);

		if(FileTools.isDirectory(dir) == false) {
			System.out.println("ログフォルダを作成します。パス=[" + dir + "]");
			FileTools.mkdirs(dir);

			if(FileTools.isDirectory(dir) == false) {
				throw new Exception("ログフォルダを作成出来ません。");
			}
		}
		return FileTools.combine(dir, prefix);
	}

	public static Logger create(String fileBase) {
		return new Logger(new TextWriter() {
			private OutputStreamWriter _writer;

			@Override
			public void open() {
				try {
					String file = fileBase + DateTimeToSec.Now.getDateTime() + "000.log";
					file = FileTools.toCreatable(file);
					_writer = FileTools.writeOpenTextFile(file, StringTools.CHARSET_UTF8);
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}

			@Override
			public void close() {
				if(_writer != null) {
					FileTools.close(_writer);
					_writer = null;
				}
			}

			@Override
			public void writeLine(String line) {
				try {
					_writer.write(line);
					_writer.write('\r');
					_writer.write('\n');
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Logger createForResident(String dir, String prefix) throws Exception {
		return createForResident(getFileBase(dir, prefix));
	}

	public static Logger createForResident(String fileBase) {
		return new Logger(new TextWriter() {
			private String _file = null;
			private OutputStreamWriter _writer = null;

			@Override
			public void open() {
				// noop
			}

			@Override
			public void close() {
				if(_writer != null) {
					FileTools.close(_writer);
					_writer = null;
				}
			}

			@Override
			public void writeLine(String line) {
				try {
					reopen();
					_writer.write(line);
					_writer.write('\r');
					_writer.write('\n');
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}

			private void reopen() throws Exception {
				String file = fileBase + DateToDay.Today.getDate() + ".log";

				if(file.equalsIgnoreCase(_file) == false) {
					_file = file;
					_writer = FileTools.writeOpenTextFile(_file, StringTools.CHARSET_UTF8, true);
				}
			}
		});
	}

	public TextWriter _writer;

	public Logger(TextWriter writer_bind) {
		_writer = writer_bind;
		_writer.open();
	}

	public void write(Throwable e) {
		try {
			List<String> reasons = getReasons(e);

			write("エラー: " + reasons.get(0));

			for(int index = 1; index < reasons.size(); index++) {
				_writer.writeLine("\t原因(" + index + "): " + reasons.get(index));
			}
			_writer.writeError(SystemTools.toString(e));
		}
		catch(Throwable ex) {
			ex.printStackTrace();
		}
	}

	private static List<String> getReasons(Throwable e) {
		List<String> ret = new ArrayList<String>();

		while(e != null) {
			String message = e.getMessage();

			if(message == null) {
				message = e.getClass().getName();
			}
			ret.add(message);

			e = e.getCause();
		}
		return ret;
	}

	public void write(String line) {
		try {
			_writer.writeLine(TimeData.now().getString("[Y/M/D h:m:s] ") + line);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public void writeLine(String line) {
		try {
			_writer.writeLine(line);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		if(_writer != null) {
			_writer.close();
			_writer = null;
		}
	}

	public static void logSlim(String dir, long totalSizeMax, int totalCountMax) {
		List<String> files = FileTools.ls(dir);
		ArrayTools.sort(files, StringTools.compIgnoreCase);
		int delCount = 0;

		if(1 <= totalCountMax && totalCountMax < files.size()) {
			delCount = totalCountMax - files.size();
		}
		if(1L <= totalSizeMax) {
			long totalSize = 0L;

			for(int index = delCount; index < files.size(); index++) {
				totalSize += FileTools.getFileSize(files.get(index));
			}
			while(delCount < files.size() && totalSizeMax < totalSize) {
				totalSize -= FileTools.getFileSize(files.get(delCount));
				delCount++;
			}
		}
		for(int index = 0; index < delCount; index++) {
			FileTools.rm(files.get(index));
		}
	}
}
