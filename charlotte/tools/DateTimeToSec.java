package charlotte.tools;

import java.util.TimeZone;

public class DateTimeToSec {
	public static long toSec(long dateTime) {
		if(dateTime < 10000101000000L || 99991231235959L < dateTime) {
			return 0; // dummy sec
		}
		int date = (int)(dateTime / 1000000);
		int h = (int)((dateTime / 10000) % 100);
		int m = (int)((dateTime / 100) % 100);
		int s = (int)(dateTime % 100);

		if(date < 10000101 || 99991231 < date ||
				h < 0 || 23 < h ||
				m < 0 || 59 < m ||
				s < 0 || 59 < s
				) {
			return 0; // dummy sec
		}

		long sec = DateToDay.toDay(date);
		sec *= 24;
		sec += h;
		sec *= 60;
		sec += m;
		sec *= 60;
		sec += s;

		return sec;
	}

	public static long toDateTime(long sec) {
		if(sec < 0) {
			return 10000101000000L; // dummy date-time
		}
		long lDay = sec / 86400;

		if(Integer.MAX_VALUE < lDay) {
			return 10000101000000L; // dummy date-time
		}
		int h = (int)((sec / 3600) % 24);
		int m = (int)((sec / 60) % 60);
		int s = (int)(sec % 60);

		long dateTime = DateToDay.toDate((int)lDay);
		dateTime *= 100;
		dateTime += h;
		dateTime *= 100;
		dateTime += m;
		dateTime *= 100;
		dateTime += s;

		return dateTime;
	}

	public static class Now {
		public static long getSec() {
			return (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 1000 + 62135596800L;
		}

		public static long getDateTime() {
			return toDateTime(getSec());
		}
	}

	public static boolean isFairSec(long sec) {
		return toSec(toDateTime(sec)) == sec;
	}

	public static boolean isFairDateTime(long dateTime) {
		return toDateTime(toSec(dateTime)) == dateTime;
	}
}
