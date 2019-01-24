package charlotte.tools;

import java.util.TimeZone;

/**
 * 1000/1/1 ～ 9999/12/31
 *
 */
public class DateToDay {
	public static int toDay(int date) {
		if(date < 10000101 || 99991231 < date) {
			return 0; // dummy day
		}
		int y = date / 10000;
		int m = (date / 100) % 100;
		int d = date % 100;

		if(y < 1000 || 9999 < y ||
				m < 1 || 12 < m ||
				d < 1 || 31 < d
				) {
			return 0; // dummy day
		}

		if(m <= 2) {
			y--;
		}
		int day = y / 400;
		day *= 365 * 400 + 97;

		y %= 400;

		day += y * 365;
		day += y / 4;
		day -= y / 100;

		if(2 < m) {
			day -= 31 * 10 - 4;
			m -= 3;
			day += (m / 5) * (31 * 5 - 2);
			m %= 5;
			day += (m / 2) * (31 * 2 - 1);
			m %= 2;
			day += m * 31;
		}
		else {
			day += (m - 1) * 31;
		}
		day += d - 1;
		return day;
	}

	public static int toDate(int day) {
		if(day < 0) {
			return 10000101; // dummy date
		}
		int y = (day / 146097) * 400 + 1;
		int m = 1;
		int d;
		day %= 146097;

		day += Math.min((day + 306) / 36524, 3);
		y += (day / 1461) * 4;
		day %= 1461;

		day += Math.min((day + 306) / 365, 3);
		y += day / 366;
		day %= 366;

		if(60 <= day) {
			m += 2;
			day -= 60;
			m += (day / 153) * 5;
			day %= 153;
			m += (day / 61) * 2;
			day %= 61;
		}
		m += day / 31;
		day %= 31;
		d = day + 1;

		if(y < 1000 || 9999 < y ||
				m < 1 || 12 < m ||
				d < 1 || 31 < d
				) {
			return 10000101; // dummy date
		}

		return y * 10000 + m * 100 + d;
	}

	public static class Today {
		public static int getDay() {
			return (int)((System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 86400000) + 719162;
		}

		public static int getDate() {
			return toDate(getDay());
		}
	}

	public static boolean isFairDay(int day) {
		return toDay(toDate(day)) == day;
	}

	public static boolean isFairDate(int date) {
		return toDate(toDay(date)) == date;
	}
}
