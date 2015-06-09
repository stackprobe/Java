package charlotte_test.tools;

import charlotte.tools.ReflecTools;
import charlotte.tools.TimeData;

public class ReflecToolsTest {
	public static void main(String[] args) {
		try {
			TimeData td = TimeData.now();

			System.out.println("td: " + td);

			long t = (Long)ReflecTools.getObject(
					ReflecTools.getDeclaredField(Class.forName("charlotte.tools.TimeData"), "_t"),
					td
					);

			System.out.println("t: " + t);
			t++;
			System.out.println("t: " + t);

			ReflecTools.setObject(
					ReflecTools.getDeclaredField(Class.forName("charlotte.tools.TimeData"), "_t"),
					td,
					new Long(t)
					);

			System.out.println("td: " + td);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
