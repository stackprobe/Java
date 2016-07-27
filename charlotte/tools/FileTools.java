package charlotte.tools;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FileTools {
	public static List<String> readAllLines(String file, String charset) throws Exception {
		String text = readAllText(file, charset);
		text = text.replace("\r", "");
		List<String> lines = StringTools.tokenize(text, "\n");

		if(1 <= lines.size() && lines.get(lines.size() - 1).length() == 0) {
			lines.remove(lines.size() - 1);
		}
		return lines;
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

	public static void writeAllLines(String file, String[] lines, String charset) throws Exception {
		writeAllLines(file, Arrays.asList(lines), charset);
	}

	public static void writeAllLines(String file, List<String> lines, String charset) throws Exception {
		writeAllText(file, StringTools.join("\r\n", lines) + "\r\n", charset);
	}

	public static void writeAllText(String file, String text, String charset) throws Exception {
		writeAllBytes(file, text.getBytes(charset));
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
		try {
			new File(path).delete();
		}
		catch(Throwable e) {
			// ignore e
		}
	}

	public static void del(File f) {
		try {
			f.delete();
		}
		catch(Throwable e) {
			// ignore e
		}
	}

	public static boolean exists(String path) {
		return new File(path).exists();
	}

	public static void mkdir(String dir) {
		new File(dir).mkdir();
	}

	public static void mkdirs(String dir) {
		new File(dir).mkdirs();
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

	public static void rm(String path) {
		File f = new File(path);

		if(f.isDirectory()) {
			for(String s : f.list()) {
				rm(FileTools.combine(path, s));
			}
		}
		del(f);
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

	public static String readLine(InputStream is) throws Exception {
		return readLine(is, StringTools.CHARSET_UTF8);
	}

	public static String readLine(InputStream is, String charset) throws Exception {
		ByteBuffer buff = new ByteBuffer();

		for(; ; ) {
			int chr = is.read();

			// XXX charset == Utf16 とかアウト

			// ? cr
			if(chr == 0x0d) {
				continue;
			}
			// ? lf
			if(chr == 0x0a) {
				break;
			}
			buff.add((byte)chr);
		}
		return new String(buff.getBytes(), charset);
	}

	public static void writeLine(OutputStream os, String line) throws Exception {
		writeLine(os, line, StringTools.CHARSET_UTF8);
	}

	public static void writeLine(OutputStream os, String line, String charset) throws Exception {
		os.write(line.getBytes(charset));
		os.write("\r\n".getBytes(charset));
	}

	public static String getLocal(String path) {
		int index = StringTools.lastIndexOfChar(path, "/\\");

		if(index == -1) {
			return path;
		}
		return path.substring(index + 1);
	}

	public static String eraseLocal(String path) {
		int index = StringTools.lastIndexOfChar(path, "/\\");

		if(index == -1) {
			return ".";
		}
		return path.substring(0, index);
	}

	public static String getExt(String path) {
		path = getLocal(path);

		int index = path.lastIndexOf('.');

		if(index == -1) {
			return "";
		}
		return path.substring(index + 1);
	}

	public static String getDotExt(String path) {
		String ext = getExt(path);

		if(ext.length() == 0) {
			return "";
		}
		return "." + ext;
	}

	public static String eraseExt(String path) {
		int index;

		// XXX Urlのクエリ対策
		index = path.lastIndexOf('?');
		if(index != -1) {
			path = path.substring(0, index);
		}

		index = path.lastIndexOf('.');
		if(index != -1 && StringTools.lastIndexOfChar(path, "/\\") < index) {
			path = path.substring(0, index);
		}

		return path;
	}

	public static String toCreatable(String path) {
		if(FileTools.exists(path)) {
			String prefix = FileTools.eraseExt(path) + "~";
			String suffix = FileTools.getDotExt(path);
			int c = 2;

			do {
				path = prefix + c + suffix;
				c++;
			}
			while(FileTools.exists(path));
		}
		return path;
	}

	public static String addExt(String path, String ext) {
		if(1 <= ext.length()) {
			path += "." + ext;
		}
		return path;
	}

	public static void moveFile(String rFile, String wFile) {
		File rf = new File(rFile);
		File wf = new File(wFile);

		if(rf.exists() == false) throw null; // XXX
		if(wf.exists()) throw null; // XXX

		rf.renameTo(wf);

		if(rf.exists()) throw null; // XXX
		if(wf.exists() == false) throw null; // XXX
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

	public static int lsCount(String dir) {
		return list(dir).length;
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
		// XXX グレーゾーン {
		wrf.add("COM0");
		wrf.add("LPT0");
		wrf.add("CLOCK$");
		wrf.add("CONFIG$");
		// }

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

	public static long getFileSize(String file) {
		return new File(file).length();
	}

	public static long getDiskFree(String dir) {
		return new File(dir).getFreeSpace();
	}

	public static class CmdDirHeadTail {
		public char driveChr;
		public String volumeLabel;
		public String vsn;
		public long fileCount;
		public long totalFileSize;
		public long dirCount;
		public long diskFree;
	}

	public static CmdDirHeadTail cmdDir_getHeadTail(String dir, boolean subDirFlag) throws Exception {
		CmdDirHeadTail ret = cmdDir_getHeadTail(dir, subDirFlag, true);
		CmdDirHeadTail ret2 = cmdDir_getHeadTail(dir, subDirFlag, false);

		ret.dirCount = ret2.dirCount;
		ret.diskFree = ret2.diskFree;

		return ret;
	}

	public static CmdDirHeadTail cmdDir_getHeadTail(String dir, boolean subDirFlag, boolean fileFlag) throws Exception {
		CmdDirHeadTail ret = new CmdDirHeadTail();

		dir = dir.replace('/', '\\');

		String batFile = null;
		String outFile = null;
		String outFile2 = null;
		try {
			batFile = makeTempPath() + ".bat";
			outFile = makeTempPath();
			outFile2 = makeTempPath();

			writeAllLines(
					batFile,
					new String[] {
							"SET DIRCMD=",
							"DIR \"" + dir + "\" /A" + (fileFlag ? "-" : "") + "D " + (subDirFlag ? "/S " : "") + "> \"" + outFile + "\"",
					},
					StringTools.CHARSET_SJIS
					);
			Runtime.getRuntime().exec("CMD /C \"" + batFile + "\"").waitFor();

			if(FileTools.getFileSize(outFile) != 0L) {
				writeHead(outFile, outFile2, 1000L);
				toTail(outFile, 1000L);

				{
					List<String> lines = readAllLines(outFile2, StringTools.CHARSET_SJIS);
					String line1 = lines.get(0);
					String line2 = lines.get(1);
					List<String> tokens1 = StringTools.tokenize(line1, " ", false, true);
					List<String> tokens2 = StringTools.tokenize(line2, " ", false, true);

					ret.driveChr = tokens1.get(1).charAt(0);

					{
						int labelha = line1.indexOf("ラベルは");

						if(labelha != -1) {
							ret.volumeLabel = line1.substring(labelha + 5, line1.length() - 3);
						}
					}

					ret.vsn = tokens2.get(2).replace("-", "").toLowerCase();
				}

				{
					List<String> lines = readAllLines(outFile, StringTools.CHARSET_SJIS);
					String line2 = lines.get(lines.size() - 1);

					if(line2.length() != 0) {
						String line1 = lines.get(lines.size() - 2);
						List<String> tokens1 = StringTools.tokenize(line1, " ", false, true);
						List<String> tokens2 = StringTools.tokenize(line2, " ", false, true);

						String t1 = tokens1.get(0);
						String t2 = tokens1.get(2);
						String t3 = tokens2.get(0);
						String t4 = tokens2.get(2);

						t1 = t1.replace(",", "");
						t2 = t2.replace(",", "");
						t3 = t3.replace(",", "");
						t4 = t4.replace(",", "");

						long v1 = Long.parseLong(t1);
						long v2 = Long.parseLong(t2);
						long v3 = Long.parseLong(t3);
						long v4 = Long.parseLong(t4);

						ret.fileCount = v1;
						ret.totalFileSize = v2;
						ret.dirCount = v3;
						ret.diskFree = v4;
					}
				}
			}
		}
		finally {
			del(batFile);
			del(outFile);
		}
		return ret;
	}

	public static String readLine(InputStreamReader reader) throws Exception {
		StringBuffer buff = new StringBuffer();

		for(; ; ) {
			int chr = reader.read();

			if(chr == -1) {
				if(buff.length() == 0) {
					return null;
				}
				break;
			}
			if(chr == '\r') {
				continue;
			}
			if(chr == '\n') {
				break;
			}
			buff.append((char)chr);
		}
		return buff.toString();
	}

	public static void writeLine(OutputStreamWriter writer, String line) throws Exception {
		writer.write(line);
		writer.write(0x0d);
		writer.write(0x0a);
	}

	public static void toTail(String rwFile, long tailSize) throws Exception {
		String midFile = makeTempPath();
		try {
			writeTail(rwFile, midFile, tailSize);
			copyFile(midFile, rwFile);
		}
		finally {
			del(midFile);
		}
	}

	private static void writeHead(String rFile, String wFile, long headSize) throws Exception {
		long size = getFileSize(rFile);

		if(headSize < size) {
			writePart(rFile, wFile, 0, headSize);
		}
		else {
			copyFile(rFile, wFile);
		}
	}

	private static void writeTail(String rFile, String wFile, long tailSize) throws Exception {
		long size = getFileSize(rFile);

		if(tailSize < size) {
			writePart(rFile, wFile, size - tailSize, tailSize);
		}
		else {
			copyFile(rFile, wFile);
		}
	}

	private static void writePart(String rFile, String wFile, long startPos, long size) throws Exception {
		writePart(rFile, wFile, startPos, size, 1000000); // 1 MB
	}

	private static void writePart(String rFile, String wFile, long startPos, long size, int buffSize) throws Exception {
		byte[] buff = new byte[buffSize];

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(rFile);
			fis.skip(startPos);
			fos = new FileOutputStream(wFile);

			for(long count = 0; count < size; ) {
				int readSize = (int)Math.min((long)buff.length, size - count);

				if(fis.read(buff, 0, readSize) != readSize) {
					throw new Exception("read error");
				}
				fos.write(buff, 0, readSize);
				count += readSize;
			}
		}
		finally {
			FileTools.close(fis);
			FileTools.close(fos);
		}
	}
}
