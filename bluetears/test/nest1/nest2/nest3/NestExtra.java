package bluetears.test.nest1.nest2.nest3;

import java.io.IOException;

import charlotte.saber.htt.HttSaberExtra;
import charlotte.tools.MathTools;
import charlotte.tools.ThreadTools;

public class NestExtra implements HttSaberExtra {
	@Override
	public boolean needToMaintenance() {
		boolean ret = MathTools.random(5) == 0;
		System.out.println("needToMaintenance: " + ret);
		return ret;
	}

	@Override
	public void maintenance() {
		System.out.println("MAINTENANCE START...");
		ThreadTools.sleep(10000L);
		System.out.println("MAINTENANCE ENDED");
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
