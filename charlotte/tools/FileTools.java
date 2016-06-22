package charlotte.tools;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileTools {
	public static List<String> readAllLines(String file, String charset) throws Exception {
		String text = readAllText(file, charset);
		text = text.replace("\r", "");
		StringTools.removeEndsWith(text, "\n");
		return StringTools.tokenize(text, "\n");
	}

	public static String readAllText(String file, String charset) throws Exception {
		return new String(readAllBytes(file), charset);
	}

	public static byte[] readAllBytes(String file) throws Exception {
		return readAllBytes(new File(file));
	}

	public static byte[] readAllBytes(File f) throws Exception {
		long lFileSize = f.length();

		if(Integer.MAX_VALUE < lFileSize) {
			throw new Exception("too_big");
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
		boolean networkPath = path1.startsWith("\\\\") || path1.startsWith("//");

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
		return readToEnd(is, Integer.MAX_VALUE);
	}

	public static byte[] readToEnd(InputStream is, int size) throws Exception {
		ByteBuffer buff = new ByteBuffer();

		for(int count = 0; count < size; count++) {
			int chr = is.read();

			if(chr == -1) {
				break;
			}
			buff.add((byte)chr);
		}
		return buff.getBytes();
	}

	public static byte[] readToSize(InputStream is, int size) throws Exception {
		return readToSize(is, size, false);
	}

	public static byte[] readToSize(InputStream is, int size, boolean readZeroKeepReading) throws Exception {
		byte[] buff = new byte[size];
		int waitMillis = 0;
		int wPos = 0;

		while(wPos < size) {
			int readSize = is.read(buff, wPos, buff.length - wPos);

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
				wPos += readSize;
			}
		}
		if(wPos < size) {
			buff = ArrayTools.getBytes(buff, 0, wPos);
		}
		return buff;
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

	public static String eraseExt(String path) {
		int index;

		// XXX Urlのクエリ対策
		index = path.lastIndexOf('?');
		if(index != -1) {
			path = path.substring(0, index);
		}

		index = StringTools.lastIndexOf(path, new String[] { "/", "\\", "." });
		if(index != -1 && path.charAt(index) == '.') {
			path = path.substring(0, index);
		}

		return path;
	}

	public static String addExt(String path, String ext) {
		if(1 <= ext.length()) {
			path += "." + ext;
		}
		return path;
	}

	public static void copyFile(String rFile, String wFile) throws Exception {
		copyFile(rFile, wFile, 1000000); // 1 MB
	}

	public static void copyFile(String rFile, String wFile, int buffSize) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(rFile);
			fos = new FileOutputStream(wFile);

			byte[] buff = new byte[buffSize];

			for(; ; ) {
				int readSize = fis.read(buff);

				if(readSize < 0) {
					break;
				}
				fos.write(buff, 0, readSize);
			}
		}
		finally {
			FileTools.close(fis);
			FileTools.close(fos);
		}
	}

	public static boolean isSameFile(String file1, String file2) throws Exception {
		InputStream is1 = null;
		InputStream is2 = null;
		BufferedInputStream bis1 = null;
		BufferedInputStream bis2 = null;
		try {
			bis1 = new BufferedInputStream(is1 = new FileInputStream(file1));
			bis2 = new BufferedInputStream(is2 = new FileInputStream(file2));

			for(; ; ) {
				int chr1 = bis1.read();
				int chr2 = bis2.read();

				if(chr1 != chr2) {
					return false;
				}
				if(chr1 == -1) {
					break;
				}
			}
		}
		finally {
			FileTools.close(is1);
			FileTools.close(is2);
			FileTools.close(bis1);
			FileTools.close(bis2);
		}
		return true;
	}

	public static List<String> ls(String dir) {
		return ls(dir, new ArrayList<String>());
	}

	public static List<String> ls(String dir, List<String> dest) {
		for(String lPath : list(dir)) {
			dest.add(FileTools.combine(dir, lPath));
		}
		return dest;
	}

	public static List<String> lss(String dir) {
		List<String> dest = ls(dir);

		for(int index = 0; index < dest.size(); index++) {
			if(isDirectory(dest.get(index))) {
				ls(dest.get(index), dest);
			}
		}
		return dest;
	}

	public static void copyDir(String rDir, String wDir) throws Exception {
		if(exists(wDir) == false) {
			mkdir(wDir);
		}
		for(String lPath : list(rDir)) {
			String rPath = combine(rDir, lPath);
			String wPath = combine(wDir, lPath);

			if(isDirectory(rPath)) {
				copyDir(rPath, wPath);
			}
			else {
				copyFile(rPath, wPath);
			}
		}
	}

	public static Set<String> windowsReservedFiles;

	static {
		Set<String> wrf = SetTools.createIgnoreCase();

		wrf.add("AUX");
		wrf.add("CON");
		wrf.add("NUL");
		wrf.add("PRN");

		for(int c = 1; c < 9; c++) {
			wrf.add("COM" + c);
			wrf.add("LPT" + c);
		}
		wrf.add("COM0"); // XXX
		wrf.add("LPT0"); // XXX
		wrf.add("CLOCK$"); // XXX
		wrf.add("CONFIG$"); // XXX

		windowsReservedFiles = wrf;
	}

	public static final int PATH_MAX = 250;
	public static final String PATH_NG_CHRS = "\"*/:<>?\\|";

	public static String toFairLocalPath(String src) {
		return toFairLocalPath(src, 100);
	}

	public static String toFairLocalPath(String str, int dirSize) {
		int lenmax = PATH_MAX - dirSize;

		if(str == null) {
			str = "$null";
		}
		str = StringTools.charsetFilter(str, StringTools.CHARSET_SJIS);

		if(lenmax < str.length()) {
			str = str.substring(0, lenmax);
		}
		List<String> nodes = StringTools.tokenize(str, ".");

		for(int index = 0; index < nodes.size(); index++) {
			String node = nodes.get(index);

			node = node.trim(); // XXX

			{
				StringBuffer buff = new StringBuffer();

				for(char chr : node.toCharArray()) {
					if(chr < ' ' || StringTools.contains(PATH_NG_CHRS, chr)) {
						chr = '$';
					}
					buff.append(chr);
				}
				node = buff.toString();
			}

			nodes.set(index, node);
		}
		if(windowsReservedFiles.contains(nodes.get(0))) {
			nodes.set(0, "$" + nodes.get(0).substring(1));
		}
		str = StringTools.join(".", nodes);

		if(str.length() == 0) {
			str = "$";
		}
		if(str.charAt(str.length() - 1) == '.') {
			str = str.substring(0, str.length() - 1) + "$";
		}
		return str;
	}
}
