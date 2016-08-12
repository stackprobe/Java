package charlotte.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTools {
	public static void extract(String zipFile, String wDir) throws Exception {
		InputStream reader = new FileInputStream(zipFile);
		try {
			extract(reader, wDir);
		}
		finally {
			FileTools.close(reader);
		}
	}

	public static void extract(InputStream reader, String wDir) throws Exception {
		FileTools.rm(wDir);
		FileTools.mkdirs(wDir);

		ZipInputStream zReader = new ZipInputStream(reader);
		try {

			for(; ; ) {
				ZipEntry entry = zReader.getNextEntry();

				if(entry == null) {
					break;
				}
				if(entry.isDirectory()) {
					String relPath = entry.getName();
					String dir = FileTools.combine(wDir, relPath);

					System.out.println("z>d " + dir);

					FileTools.mkdirs(dir);
				}
				else {
					String relPath = entry.getName();
					String file = FileTools.combine(wDir, relPath);
					String dir = FileTools.eraseLocal(file);

					System.out.println("z>f " + file);

					FileTools.mkdirs(dir);
					FileTools.writeToEnd(zReader, file);
				}
			}
		}
		finally {
			FileTools.close(zReader);
		}
	}

	public static void pack(String rDir, String zipFile) throws Exception {
		pack(rDir, zipFile, "");
	}

	public static void pack(String rDir, String zipFile, String wBasePath) throws Exception {
		OutputStream writer = new FileOutputStream(zipFile);
		try {
			pack(rDir, writer, wBasePath);
		}
		finally {
			FileTools.close(writer);
		}
	}

	public static void pack(String rDir, OutputStream writer) throws Exception {
		pack(rDir, writer, "");
	}

	public static void pack(String rDir, OutputStream writer, String wBasePath) throws Exception {
		ZipOutputStream zWriter = new ZipOutputStream(writer);
		try {
			pk(rDir, zWriter, wBasePath);
		}
		finally {
			FileTools.close(zWriter);
		}
	}

	private static void pk(String rDir, ZipOutputStream zWriter, String wBasePath) throws Exception {
		Stack<QueueData<String>> parents = new Stack<QueueData<String>>();
		QueueData<String> lPaths = new QueueData<String>(FileTools.list(rDir));

		for(; ; ) {
			while(lPaths.size() != 0) {
				String lPath = lPaths.poll();
				String rPath = FileTools.combine(rDir, lPath);
				String wPath = FileTools.combine(wBasePath, lPath);

				if(FileTools.isDirectory(rPath)) {
					zWriter.putNextEntry(new ZipEntry(wPath + "/"));
					zWriter.closeEntry();

					parents.add(lPaths);

					lPaths = new QueueData<String>(FileTools.list(rPath));
					rDir = rPath;
					wBasePath = wPath;
				}
				else {
					zWriter.putNextEntry(new ZipEntry(wPath));
					FileTools.writeToEnd(rPath, zWriter);
					zWriter.closeEntry();
				}
			}
			if(parents.size() == 0) {
				break;
			}
			lPaths = parents.pop();
			rDir = FileTools.eraseLocal(rDir);
			wBasePath = FileTools.eraseLocal(wBasePath);
		}
		/*
		Stack<PkInfo> parents = new Stack<PkInfo>();
		PkInfo i = new PkInfo();

		i.rDir = rDir;
		i.wBasePath = wBasePath;
		i.lPaths = FileTools.list(rDir);
		i.index = 0;

		for(; ; ) {
			while(i.index < i.lPaths.length) {
				String lPath = i.lPaths[i.index];
				String rPath = FileTools.combine(i.rDir, lPath);
				String wPath = FileTools.combine(i.wBasePath, lPath);

				i.index++;

				if(FileTools.isDirectory(rPath)) {
					zWriter.putNextEntry(new ZipEntry(wPath + "/"));
					zWriter.closeEntry();

					parents.add(i);

					i = new PkInfo();
					i.rDir = rPath;
					i.wBasePath = wPath;
					i.lPaths = FileTools.list(rPath);
					i.index = 0;
				}
				else {
					zWriter.putNextEntry(new ZipEntry(wPath));
					FileTools.writeToEnd(rPath, zWriter);
					zWriter.closeEntry();
				}
			}
			if(parents.size() == 0) {
				break;
			}
			i = parents.pop();
		}
		*/
		/*
		for(String lPath : FileTools.list(rDir)) {
			String rPath = FileTools.combine(rDir, lPath);
			String wPath = FileTools.combine(wBasePath, lPath);

			System.out.println("z< " + rPath);

			if(FileTools.isDirectory(rPath)) {
				zWriter.putNextEntry(new ZipEntry(wPath + "/"));
				zWriter.closeEntry();

				pk(rPath, zWriter, wPath);
			}
			else {
				zWriter.putNextEntry(new ZipEntry(wPath));
				FileTools.writeToEnd(rPath, zWriter);
				zWriter.closeEntry();
			}
		}
		*/
	}

	/*
	private static class PkInfo {
		public String rDir;
		public String wBasePath;
		public String[] lPaths;
		public int index;
	}
	*/
}
