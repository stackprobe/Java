package charlotte.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JDate {
	public String gg;
	public String g;
	public int nen;
	public int y;
	public int m;
	public int d;

	public static JDate forDay(int day) {
		return forDate(DateToDay.toDate(day));
	}

	public static JDate forDate(int date) {
		JDate ret = new JDate();

		ret.y = date / 10000;
		ret.m = (date / 100) % 100;
		ret.d = date % 100;
		ret.gggn();

		return ret;
	}

	public static JDate forDate(int y, int m, int d) {
		JDate ret = new JDate();

		ret.y = y;
		ret.m = m;
		ret.d = d;
		ret.gggn();

		return ret;
	}

	/**
	 * { y, m, d } -> { gg, g, nen }
	 */
	public void gggn() {
		Locale locale = new Locale("ja", "JP", "JP");
		Calendar calendar = Calendar.getInstance();
		calendar.set(y, m - 1, d);
		Date date = calendar.getTime();
		gg = new SimpleDateFormat("GGGG", locale).format(date);
		g = new SimpleDateFormat("G", locale).format(date);
		nen = Integer.parseInt(new SimpleDateFormat("y", locale).format(date));
	}

	public int getDate() {
		return y * 10000 + m * 100 + d;
	}

	public int getDay() {
		return DateToDay.toDay(getDate());
	}

	public JDate fair() {
		return forDay(getDay());
	}

	public static class Period {
		public JDate firstDate;
		public JDate lastDate;
	}

	public static List<Period> getPeriods() {
		List<Period> ret = new ArrayList<Period>();
		JDate date = forDate(10000101);

		date = getPeriodLastDate(date, JDate.forDate(99991231));

		do {
			date = JDate.forDay(date.getDay() + 1);

			Period period = new Period();

			period.firstDate = date;
			period.lastDate = getPeriodLastDate(date, JDate.forDate(99991231));

			ret.add(period);

			date = period.lastDate;
		}
		while(date != null);

		return ret;
	}

	private static JDate getPeriodLastDate(JDate l, JDate r) {
		if(l.gg.equals(r.gg)) {
			return null;
		}
		while(l.getDay() + 1 < r.getDay()) {
			JDate m = JDate.forDay((l.getDay() + r.getDay()) / 2);

			if(l.gg.equals(m.gg)) {
				l = m;
			}
			else {
				r = m;
			}
		}
		return l;
	}

	@Override
	public String toString() {
		return getString("Y/M/D:g(G)n/M/D");
	}

	public String getString() {
		return getString("GN年M月D日");
	}

	private String getString(String format) {
		String strNen;

		if(nen == 1) {
			strNen = "元";
		}
		else {
			strNen = "" + nen;
		}

		String str = format;

		str = str.replace("G", gg);
		str = str.replace("N", strNen);
		str = str.replace("n", StringTools.zPad(nen, 2));
		str = str.replace("Y", StringTools.zPad(y, 4));
		str = str.replace("M", StringTools.zPad(m, 2));
		str = str.replace("D", StringTools.zPad(d, 2));
		str = str.replace("g", g);

		return str;
	}

	public static List<String> getEYYList() {
		List<String> ret = new ArrayList<String>();

		for(Period period : getPeriods()) {
			int lNen;

			if(period.lastDate != null) {
				lNen = period.lastDate.nen;
			}
			else {
				lNen = 99;
			}

			for(int nen = 1; nen <= lNen; nen++) {
				ret.add(period.firstDate.g + StringTools.zPad(nen, 2));
			}
		}
		return ret;
	}

	/**
	 *
	 * @param ggg 元号又はアルファベット
	 * @return null == 不明な元号
	 */
	public static Period getPeriod(String ggg) {
		for(Period period : getPeriods()) {
			if(ggg.contains(period.firstDate.gg) || StringTools.containsIgnoreCase(ggg, period.firstDate.g)) {
				return period;
			}
		}
		return null;
	}

	/**
	 * { ggg, nen } -> { gg, g, y }
	 *
	 * @return ? 設定した。
	 */
	public boolean gn2y(String ggg) {
		Period period = getPeriod(ggg);

		if(period != null) {
			gg = period.firstDate.gg;
			g = period.firstDate.g;
			y = period.firstDate.y + nen - 1;
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param ggg 元号又はアルファベット
	 * @param nen 年
	 * @return null == 不明な元号
	 */
	public static JDate forWareki(String ggg, int nen) {
		Period period = getPeriod(ggg);

		if(period != null) {
			JDate ret = new JDate();

			ret.gg = period.firstDate.gg;
			ret.g = period.firstDate.g;
			ret.nen = 1;
			ret.y = period.firstDate.y;
			ret.m = period.firstDate.m;
			ret.d = period.firstDate.d;

			return ret;
		}
		return null;
	}

	/**
	 *
	 * @param ggg 元号又はアルファベット
	 * @param nen 年
	 * @param m 月
	 * @param d 日
	 * @return null == 不明な元号
	 */
	public static JDate forWareki(String ggg, int nen, int m, int d) {
		JDate ret = new JDate();

		ret.nen = nen;
		ret.m = m;
		ret.d = d;

		if(ret.gn2y(ggg) == false) {
			return null;
		}
		return ret;
	}

	/**
	 *
	 * @param str 和暦表現の文字列
	 * @return null == 不明な元号であるか、不明な年又は年月日
	 */
	public static JDate forWareki(String str) {
		str = StringTools.zenToHan(str);
		str = str.replace("元", "_1_");

		List<String> sVals = StringTools.numericTokenize(str);

		switch(sVals.size()) {
		case 1:
			return forWareki(
					str,
					Integer.parseInt(sVals.get(0))
					);
		case 3:
			return forWareki(
					str,
					Integer.parseInt(sVals.get(0)),
					Integer.parseInt(sVals.get(1)),
					Integer.parseInt(sVals.get(2))
					);
		}
		return null;
	}

	/**
	 *
	 * @param str 西暦表現の文字列
	 * @return null == 不明な文字列
	 */
	public static JDate forSeireki(String str) {
		str = StringTools.zenToHan(str);

		List<String> sVals = StringTools.numericTokenize(str);

		switch(sVals.size()) {
		case 1:
			return forDate(Integer.parseInt(sVals.get(0)));
		case 3:
			return forDate(
					Integer.parseInt(sVals.get(0)),
					Integer.parseInt(sVals.get(1)),
					Integer.parseInt(sVals.get(2))
					);
		}
		return null;
	}

	public static JDate forString(String str) {
		JDate ret = forWareki(str);

		if(ret == null) {
			ret = forSeireki(str);
		}
		return ret;
	}
}
