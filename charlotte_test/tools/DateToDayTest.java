package charlotte_test.tools;

import charlotte.tools.DateToDay;

public class DateToDayTest {
	public static void main(String[] args) {
		try {
			System.out.println("today=" + DateToDay.getDate());
			System.out.println("19000101=" + DateToDay.toDay(19000101));
			System.out.println("22000101=" + DateToDay.toDay(22000101));

			/*
			// 19000101-22000101
			for(int day = 693595; day <= 803168; day++) {
				int date = DateToDay.toDate(day);
				int rDay = DateToDay.toDay(date);

				System.out.println(day + " -> " + date + " -> " + rDay);

				if(day != rDay) {
					throw new Exception("ng");
				}
			}
			*/

			System.out.println("10000101=" + DateToDay.toDay(10000101));
			System.out.println("99991231=" + DateToDay.toDay(99991231));

			//System.out.println("400000->" + DateToDay.toDate(400000));
			//System.out.println("999999->" + DateToDay.toDate(999999));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
