package charlotte.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;

public class SystemTools {
	public static final int PID = Integer.parseInt(
			ManagementFactory.getRuntimeMXBean().getName().split("@")[0]
			);

	public static long getProcessId(Process proc) throws Exception {
		return ((Long)ReflecTools.getObject(
				ReflecTools.getDeclaredField(proc.getClass(), "handle"),
				proc
				))
				.longValue();
	}

	public static String toString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String ret = sw.toString();
		return ret;
	}

	public static void execOnBatch(String commandLine) throws Exception {
		execOnBatch(new String[] { commandLine });
	}

	public static void execOnBatch(String[] commandLines) throws Exception {
		String batFile = FileTools.makeTempPath() + ".bat";
		try {
			FileTools.writeAllLines(batFile, commandLines, StringTools.CHARSET_SJIS);
			Runtime.getRuntime().exec("CMD /C \"" + batFile + "\"").waitFor();
		}
		finally {
			FileTools.del(batFile);
		}
	}

	public static String getHostName() throws Exception {
		String ret = InetAddress.getLocalHost().getHostName();

		if(StringTools.isEmpty(ret)) {
			ret = getHostIP();
		}
		return ret;
	}

	public static String getHostIP() throws Exception {
		return toString_ip(InetAddress.getLocalHost().getAddress());
	}

	public static String toString_ip(byte[] ip) {
		return (ip[0] & 0xff) + "." +
				(ip[1] & 0xff) + "." +
				(ip[2] & 0xff) + "." +
				(ip[3] & 0xff);
	}
}
