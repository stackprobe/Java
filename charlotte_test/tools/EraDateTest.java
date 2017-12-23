package charlotte_test.tools;

import charlotte.tools.EraDate;
import charlotte.tools.MathTools;
import charlotte.tools.TimeData;

public class EraDateTest {
	public static void main(String[] args) {
		try {
			long te = TimeData.now().getTime() + 200000000;

			for(long t = 0; t <= te; t += MathTools.random(50000000)) {
				TimeData td = new TimeData(t);

				System.out.println(td.getString("Y/M/D") + " == " + new EraDate(td).getString());
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
