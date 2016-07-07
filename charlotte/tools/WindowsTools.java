package charlotte.tools;

import java.util.List;
import java.util.Map;

public class WindowsTools {
	private static Map<String, String> getEnvs() throws Exception {
		Map<String, String> ret = MapTools.<String>createIgnoreCase();
		String batFile = null;
		String outFile = null;

		try {
			batFile = FileTools.makeTempPath() + ".bat";
			outFile = FileTools.makeTempPath();
			FileTools.writeAllText(batFile, "SET > \"" + outFile + "\"", StringTools.CHARSET_SJIS);
			Runtime.getRuntime().exec("CMD /C \"" + batFile + "\"").waitFor();
			List<String> lines = FileTools.readAllLines(outFile, StringTools.CHARSET_SJIS);

			for(String line : lines) {
				int index = line.indexOf('=');
				if(index == -1) {
					throw new RuntimeException("SET stdout format error");
				}
				String name = line.substring(0, index);
				String value = line.substring(index + 1);

				if(StringTools.isEmpty(name)) {
					throw new RuntimeException("SET stdout format error");
				}
				if(StringTools.isEmpty(value)) {
					throw new RuntimeException("SET stdout format error");
				}
				ret.put(name, value);
			}
		}
		finally {
			FileTools.del(batFile);
			batFile = null;
			FileTools.del(outFile);
			outFile = null;
		}
		return ret;
	}

	public static String getEnv(String name) throws Exception {
		return getEnv(name, null);
	}

	private static Map<String, String> _envs = null;

	public static String getEnv(String name, String defval) throws Exception {
		if(_envs == null) {
			_envs = getEnvs();
		}
		String value = _envs.get(name);

		if(StringTools.isEmpty(value)) {
			value = defval;
		}
		return value;
	}

	/**
	 * [注意](空の|存在しない)ディレクトリの場合、空のファイルになる -> readAllLines で読み込むと、"" の1行が読める。
	 * [注意]存在するファイルの場合、ファイルのローカル名1行が出力される。
	 * @param dir
	 * @param outFile
	 * @throws Exception
	 */
	public static void ls(String dir, String outFile) throws Exception {
		dir = dir.replace('/', '\\');
		FileTools.rm(outFile);

		{
			String batFile = null;

			try {
				batFile = FileTools.makeTempPath() + ".bat";
				FileTools.writeAllText(batFile, "DIR \"" + dir + "\" /B > \"" + outFile + "\"", StringTools.CHARSET_SJIS);
				Runtime.getRuntime().exec("CMD /C \"" + batFile + "\"").waitFor();
			}
			finally {
				FileTools.del(batFile);
				batFile = null;
			}
		}

		if(FileTools.exists(outFile) == false) {
			throw new Exception("DIR fault");
		}
	}
}
