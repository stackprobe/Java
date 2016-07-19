package charlotte.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeData {
	private long _t;

	public TimeData(long t) {
		_t = t;
	}

	public TimeData(int y, int m, int d) {
		_t = getTime(y, m, d, 0, 0, 0);
	}

	public TimeData(int y, int m, int d, int h, int i, int s) {
		_t = getTime(y, m, d, h, i, s);
	}

	public TimeData(Calendar cal) {
		this(cal.getTime());
	}

	@SuppressWarnings("deprecation")
	public TimeData(Date src) {
		_t = getTime(
				src.getYear() + 1900,
				src.getMonth() + 1,
				src.getDate(),
				0,
				0,
				0
				);
	}

	public long getTime() {
		return _t;
	}

	public int[] getTimeStamp() {
		return getTimeStamp(_t);
	}

	/**
	 *
	 * @return 0-6 as Mon-Sun
	 */
	public int getWeekdayIndex() {
		return (int)((_t / 86400) % 7);
	}

	private static final String[] _weekdays = new String[]{
		"月",
		"火",
		"水",
		"木",
		"金",
		"土",
		"日",
	};

	public static String getWeekday(int index) {
		return _weekdays[index];
	}

	public String getWeekday() {
		return _weekdays[getWeekdayIndex()];
	}

	@Override
	public String toString() {
		try {
			return getString("Y/M/D (W) h:m:s");
		}
		catch(Throwable e) {
			// ignore
		}
		return "" + _t;
	}

	public static long getTime(int y, int m, int d, int h, int i, int s) {
		if(
				y < 1 ||
				m < 1 ||
				d < 1 ||
				h < 0 ||
				i < 0 ||
				s < 0
				) {
			return 0;
		}
		m--;
		long ly = (long)y + m / 12;
		m %= 12;
		m++;

		if(m <= 2) {
			ly--;
		}
		long t = ly / 400;

		t *= 365 * 400 + 97;
		ly %= 400;
		t += ly * 365;
		t += ly / 4;
		t -= ly / 100;

		if(2 < m) {
			t -= 31 * 10 - 4;
			m -= 3;
			t += (m / 5) * (31 * 5 - 2);
			m %= 5;
			t += (m / 2) * (31 * 2 - 1);
			m %= 2;
			t += m * 31;
		}
		else {
			t += (m - 1) * 31;
		}
		t += d - 1;
		t *= 24;
		t += h;
		t *= 60;
		t += i;
		t *= 60;
		t += s;

		return t;
	}

	public static int[] getTimeStamp(long t) {
		if(t < 0) {
			return new int[]{ 1, 1, 1, 0, 0, 0 };
		}
		int s = (int)(t % 60);
		t /= 60;
		int i = (int)(t % 60);
		t /= 60;
		int h = (int)(t % 24);
		t /= 24;
		long ly = (t / 146097) * 400 + 1;

		t %= 146097;

		t += Math.min((t + 306) / 36524, 3);
		ly += (t / 1461) * 4;
		t %= 1461;

		t += Math.min((t + 306) / 365, 3);
		ly += t / 366;
		t %= 366;

		int m = 1;

		if(60 <= t) {
			m += 2;
			t -= 60;
			m += (t / 153) * 5;
			t %= 153;
			m += (t / 61) * 2;
			t %= 61;
		}
		m += t / 31;
		t %= 31;

		if(Integer.MAX_VALUE <= ly) {
			return new int[]{ Integer.MAX_VALUE, 12, 31, 23, 59, 59 };
		}
		int y = (int)ly;
		int d = (int)t + 1;

		return new int[]{ y, m, d, h, i, s };
	}

	public static TimeData now() throws Exception {
		return fromEpochTime((System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 1000);
	}

	public static TimeData utc() throws Exception {
		return fromEpochTime(System.currentTimeMillis() / 1000);
	}

	public static final TimeData EPOCH_TIME_ZERO = new TimeData(1970, 1, 1);

	public static TimeData fromEpochTime(long sec) throws Exception {
		return new TimeData(EPOCH_TIME_ZERO.getTime() + sec);
	}

	public long getEpochTime() {
		return _t - EPOCH_TIME_ZERO._t;
	}

	public static TimeData fromISO8061(String str) throws Exception {
		str = StringTools.tokenize(str, "/").get(0); // ignore interval

		List<String> tokens = StringTools.tokenize(str, "T");

		String datePart = tokens.get(0);
		String timePart = tokens.size() < 2 ? "00:00:00" : tokens.get(1);
		int tzSign = 0;
		String tzPart = "00:00";

		datePart = datePart.replace("-", "");
		timePart = timePart.replace("Z", "+00:00");
		timePart = timePart.replace(":", "");
		timePart = timePart.replace(",", ".");

		{
			int index = StringTools.indexOf(timePart, new String[]{ "+", "-" });

			if(index != -1) {
				tzSign = timePart.charAt(index) == '-' ? -1 : 1;
				tzPart = timePart.substring(index + 1);
				timePart = timePart.substring(0, index);
			}
		}

		tzPart = tzPart.replace(":", "");

		String dateFormat = getFormat(datePart);
		String timeFormat = getFormat(timePart);
		String tzFormat = getFormat(tzPart);

		timeFormat = StringTools.replace(timeFormat, ".99", ".9", 19);

		int y = 1;
		int m = 1;
		int d = 1;
		int h = 0;
		int i = 0;
		int s = 0;
		int tz = 0;

		if(dateFormat.equals("99")) {
			y = Integer.parseInt(datePart) * 100;
		}
		else if(dateFormat.equals("9999")) {
			y = Integer.parseInt(datePart);
		}
		else if(dateFormat.equals("9999999")) {
			int w = Integer.parseInt(datePart);
			y = w / 1000;
			d = w % 1000;
		}
		else if(dateFormat.equals("9999W999")) {
			y = Integer.parseInt(datePart.substring(0, 4));

			int week = Integer.parseInt(datePart.substring(5, 7)) - 1;
			int weekday = Integer.parseInt(datePart.substring(7)) - 1;

			TimeData td = new TimeData(y, 1, 4, 0, 0, 0);

			td._t -= td.getWeekdayIndex() * 86400; // to W01-1
			td._t += (week * 7 + weekday) * 86400;

			int[] c = td.getTimeStamp();

			y = c[0];
			m = c[1];
			d = c[2];
		}
		else if(dateFormat.equals("99999999")) {
			int w = Integer.parseInt(datePart);
			y = w / 10000;
			m = (w / 100) % 100;
			d = w % 100;
		}
		else {
			throw new Exception("dateFormat: " + dateFormat);
		}

		if(timeFormat.equals("99")) {
			h = Integer.parseInt(timePart);
		}
		else if(timeFormat.equals("9999")) {
			int w = Integer.parseInt(timePart);
			h = w / 100;
			i = w % 100;
		}
		else if(timeFormat.equals("999999")) {
			int w = Integer.parseInt(timePart);
			h = w / 10000;
			i = (w / 100) % 100;
			s = w % 100;
		}
		else if(timeFormat.equals("99.9")) {
			s = (int)(Double.parseDouble(timePart) * 3600.0 + 0.5);
		}
		else if(timeFormat.equals("9999.9")) {
			h = Integer.parseInt(timePart.substring(0, 2));
			s = (int)(Double.parseDouble(timePart.substring(2)) * 60.0 + 0.5);
		}
		else if(timeFormat.equals("999999.9")) {
			h = Integer.parseInt(timePart.substring(0, 2));
			i = Integer.parseInt(timePart.substring(2, 4));
			s = (int)(Double.parseDouble(timePart.substring(4)) + 0.5);
		}
		else {
			throw new Exception("timeFormat: " + timeFormat);
		}

		if(tzFormat.equals("99")) {
			tz = Integer.parseInt(tzPart) * 3600;
		}
		else if(tzFormat.equals("9999")) {
			tz =
					Integer.parseInt(tzPart.substring(0, 2)) * 3600 +
					Integer.parseInt(tzPart.substring(2)) * 60;
		}
		else {
			throw new Exception("tzFormat: " + tzFormat);
		}
		tz *= tzSign;

		TimeData result = new TimeData(y, m, d, h, i, s);

		result._t -= tz; // to UTC
		result._t += TimeZone.getDefault().getRawOffset() / 1000; // to local time

		return result;
	}

	private static String getFormat(String str) {
		return StringTools.replaceChar(str, StringTools.DIGIT, '9');
	}

	public static TimeData fromString(String str) throws Exception {
		List<String> tokens = StringTools.numericTokenize(str);

		if(tokens.size() == 1) {
			str = tokens.get(0);

			if(str.length() == 8) {
				return fromDateStamp(Integer.parseInt(str));
			}
			if(11 <= str.length() && str.length() <= 14) {
				return fromTimeStamp(Long.parseLong(str));
			}
		}
		else if(tokens.size() == 3) {
			return new TimeData(
					Integer.parseInt(tokens.get(0)),
					Integer.parseInt(tokens.get(1)),
					Integer.parseInt(tokens.get(2))
					);
		}
		else if(tokens.size() == 6) {
			return new TimeData(
					Integer.parseInt(tokens.get(0)),
					Integer.parseInt(tokens.get(1)),
					Integer.parseInt(tokens.get(2)),
					Integer.parseInt(tokens.get(3)),
					Integer.parseInt(tokens.get(4)),
					Integer.parseInt(tokens.get(5))
					);
		}
		throw new Exception("Unknown time-data format: " + str);
	}

	public static TimeData fromTimeStamp(long timeStamp) {
		return fromDateStamp(
				(int)(timeStamp / 1000000L),
				(int)(timeStamp % 1000000L)
				);
	}

	public static TimeData fromDateStamp(int dateStamp) {
		return fromDateStamp(dateStamp, 0);
	}

	public static TimeData fromTimeStamp(int[] timeStamp) {
		return new TimeData(
				timeStamp[0],
				timeStamp[1],
				timeStamp[2],
				timeStamp[3],
				timeStamp[4],
				timeStamp[5]
				);
	}

	public static TimeData fromDateStamp(int[] dateStamp) {
		return new TimeData(
				dateStamp[0],
				dateStamp[1],
				dateStamp[2]
				);
	}

	public static TimeData fromDateStamp(int dateStamp, int dayTimeStamp) {
		int d = dateStamp % 100;
		dateStamp /= 100;
		int m = dateStamp % 100;
		int y = dateStamp / 100;

		int s = dayTimeStamp % 100;
		dayTimeStamp /= 100;
		int i = dayTimeStamp % 100;
		int h = dayTimeStamp / 100;

		return new TimeData(y, m, d, h, i, s);
	}

	public String getString() {
		return getString("YMDhms");
	}

	public String getString(String format) {
		int[] timeStamp = getTimeStamp();

		if(9999 < timeStamp[0]) {
			timeStamp[0] = 9999;
			timeStamp[1] = 12;
			timeStamp[2] = 31;
			timeStamp[3] = 23;
			timeStamp[4] = 59;
			timeStamp[5] = 59;
		}
		String weekday = TimeData.fromTimeStamp(timeStamp).getWeekday();
		String ret = format;

		ret = ret.replace("Y", StringTools.zPad(timeStamp[0], 4));
		ret = ret.replace("M", StringTools.zPad(timeStamp[1], 2));
		ret = ret.replace("D", StringTools.zPad(timeStamp[2], 2));
		ret = ret.replace("h", StringTools.zPad(timeStamp[3], 2));
		ret = ret.replace("m", StringTools.zPad(timeStamp[4], 2));
		ret = ret.replace("s", StringTools.zPad(timeStamp[5], 2));
		ret = ret.replace("W", weekday);

		return ret;
	}
}
