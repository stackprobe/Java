package charlotte.tools;

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
}
