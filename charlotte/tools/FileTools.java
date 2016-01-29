package charlotte.tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class FileTools {
	public static byte[] readAllBytes(String file) throws Exception {
		return readAllBytes(new File(file));
	}

	public static byte[] readAllBytes(File f) throws Exception {
		long lFileSize = f.length();

		if(Integer.MAX_VALUE < lFileSize) {
			throw new Exception("so_big");
		}
		int fileSize = (int)lFileSize;
		byte[] fileData = new byte[fileSize];
		FileInputStream is = new FileInputStream(f);

		try {
			is.read(fileData);

			// old
			/*
			for(int i = 0; i < fileSize; i++) {
				fileData[i] = (byte)is.read();
			}
			*/
		}
		finally {
			close(is);
		}
		return fileData;
	}

	public static void writeAllBytes(String file, byte[] fileData) throws Exception {
		writeAllBytes(new File(file), fileData);
	}

	public static void writeAllBytes(File f, byte[] fileData) throws Exception {
		FileOutputStream os = new FileOutputStream(f);

		try {
			os.write(fileData);

			// old
			/*
			for(int i = 0; i < fileData.length; i++) {
				os.write((int)fileData[i]);
			}
			*/
		}
		finally {
			close(os);
		}
	}

	public static void writeAllBytes(String file, BlockBuffer fileData) throws Exception {
		writeAllBytes(new File(file), fileData);
	}

	public static void writeAllBytes(File f, BlockBuffer fileData) throws Exception {
		FileOutputStream os = new FileOutputStream(f);

		try {
			for(BlockBuffer.SubBlock sb : fileData.directGetBuff()) {
				os.write(sb.block, sb.startPos, sb.size);
			}
		}
		finally {
			close(os);
		}
	}

	public static void close(Closeable c) {
		try {
			c.close();
		}
		catch(Throwable e) {
			// ignore
		}
	}

	public static String makeTempPath() {
		return FileTools.combine(_tmpDir, StringTools.getUUID());
	}

	public static String makeTempPath(String ident) {
		return FileTools.combine(_tmpDir, ident);
	}

	public static final String _tmpDir = System.getProperty("java.io.tmpdir", "C:/temp/");

	public static String combine(String path1, String path2) {
		boolean networkPath = path1.startsWith("\\\\");

		String path = path1 + "/" + path2;

		path = path.replace('\\', '/');
		path = StringTools.replace(path, "//", "/", 10);

		if(networkPath) {
			path = "\\" + path.replace('/', '\\');
		}
		return path;
	}

	public static void delete(String path) {
		new File(path).delete();
	}

	public static void del(String path) {
		del(new File(path));
	}

	public static void del(File f) {
		try {
			f.delete();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static boolean exists(String path) {
		return new File(path).exists();
	}

	public static void mkdir(String dir) {
		new File(dir).mkdir();
	}

	public static void createFile(String file) throws Exception {
		writeAllBytes(file, new byte[0]);
	}

	/**
	 *
	 * @param dir
	 * @return dir 直下のディレクトリとファイルのローカル名の一覧
	 */
	public static String[] list(String dir) {
		return new File(dir).list();
	}

	public static boolean isEmptyDir(String dir) {
		return list(dir).length == 0;
	}

	public static void tryDeleteDir(String dir) {
		if(isEmptyDir(dir)) {
			delete(dir);
		}
	}

	public static void remove(String path) {
		File f = new File(path);

		if(f.isDirectory()) {
			for(String s : f.list()) {
				remove(FileTools.combine(path, s));
			}
		}
		f.delete();
	}

	public static boolean isDirectory(String path) {
		return new File(path).isDirectory();
	}

	public static void tryDelete(String path) {
		if(isDirectory(path) == false || isEmptyDir(path)) {
			delete(path);
		}
	}

	public static byte[] readToEnd(InputStream is) throws Exception {
		return readToEnd(is, false);
	}

	public static byte[] readToEnd(InputStream is, boolean readZeroKeepReading) throws Exception {
		BlockBuffer buff = new BlockBuffer();
		int size = 1024;
		byte[] block = new byte[size];
		int waitMillis = 0;

		for(; ; ) {
			int readSize = is.read(block, 0, block.length);

			if(readSize < 0) {
				break;
			}
			if(readSize == 0) {
				if(readZeroKeepReading == false) {
					break;
				}
				if(waitMillis < 200) {
					waitMillis++;
				}
				Thread.sleep(waitMillis);
			}
			else {
				waitMillis = 0;

				if(readSize < block.length) {
					if(1024 < size) {
						size /= 2;
					}
				}
				else {
					size *= 2;
				}
				buff.add(block, 0, readSize);
				block = new byte[size];
			}
		}
		return buff.getBytes();
	}

	/**
	 *
	 * @param url AAA.class.getResource("res/BBB.dat")
	 * @return
	 * @throws Exception
	 */
	public static byte[] readToEnd(URL url) throws Exception {
		return readToEnd(url.openStream());
	}

	public static String getLocal(String path) {
		int index = StringTools.lastIndexOf(path, new String[] { "/", "\\" });

		if(index == -1) {
			return path;
		}
		return path.substring(index + 1);
	}

	public static String getExt(String path) {
		path = getLocal(path);

		int index = path.lastIndexOf('.');

		if(index == -1) {
			return "";
		}
		return path.substring(index + 1);
	}
}
