package charlotte_test.tools;

import charlotte.tools.DateToDay;
import charlotte.tools.PublicHoliday;

public class PublicHolidayTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		int minDate = PublicHoliday.i().getMinYear() * 10000 + 101;
		int maxDate = PublicHoliday.i().getMaxYear() * 10000 + 1231;

		for(int date = minDate; date <= maxDate; date = DateToDay.toDate(DateToDay.toDay(date) + 1)) {
			if(PublicHoliday.i().isPublicHoliday(date)) {
				System.out.println(date + " は「" + PublicHoliday.i().getPublicHolidayName(date) + "」で祝日です。");
			}
			else {
				//System.out.println(date + " は祝日ではありません。");
			}
		}
	}
}
