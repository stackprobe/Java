package charlotte_test.xxxtools;

import charlotte.xxxtools.DateToDay;

public class DateToDayTest {
	public static void main(String[] args) {
		try {
			System.out.println("today=" + DateToDay.getDate());
			System.out.println("19000101=" + DateToDay.toDay(19000101));
			System.out.println("22000101=" + DateToDay.toDay(22000101));

			// 19000101-22000101
			for(int day = 693595; day <= 803168; day++) {
				int date = DateToDay.toDate(day);
				int rDay = DateToDay.toDay(date);

				System.out.println(day + " -> " + date + " -> " + rDay);

				if(day != rDay) {
					throw new Exception("ng");
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
