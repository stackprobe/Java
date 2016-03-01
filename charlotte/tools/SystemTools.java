package charlotte.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;

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
}
