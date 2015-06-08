package charlotte_test.tools;

import charlotte.tools.EraDate;
import charlotte.tools.TimeData;

public class EraDateTest {
	public static void main(String[] args) {
		try {
			System.out.println("now: " + new EraDate(TimeData.now()));
			System.out.println("19900108: " + new EraDate(1990, 1, 8));
			System.out.println("19890108: " + new EraDate(1989, 1, 8));
			System.out.println("19890107: " + new EraDate(1989, 1, 7));
			System.out.println("19790906: " + new EraDate(1979, 9, 6));
			System.out.println("19000101: " + new EraDate(1900, 1, 1));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
