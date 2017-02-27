package charlotte.tools;

import java.util.Map;
import java.util.TreeMap;

public class PublicHoliday {
	private static PublicHoliday _i = null;

	public static PublicHoliday i() {
		if(_i == null) {
			_i = new PublicHoliday();
		}
		return _i;
	}

	private PublicHoliday() {
		try {
			init();
		}
		catch(Throwable e) {
			throw RunnableEx.re(e);
		}
	}

	private static final String NAIKAKUFU_CSV_URL = "http://www8.cao.go.jp/chosei/shukujitsu/syukujitsu.csv";

	private Map<Integer, String> _map = new TreeMap<Integer, String>(IntTools.comp);
	private int _minY;
	private int _maxY;

	private void init() throws Exception {
		HTTPClient hc = new HTTPClient(NAIKAKUFU_CSV_URL);
		hc.get();
		byte[] bCsv = hc.getResBody();
		String sCsv = new String(bCsv, StringTools.CHARSET_SJIS);
		CsvData csv = new CsvData();
		csv.readText(sCsv);
		AutoTable<String> table = csv.getTable();

		_minY = Integer.MAX_VALUE;
		_maxY = Integer.MIN_VALUE;

		for(int cc = 0; cc < table.getWidth(); cc++) {
			String cell = table.get(cc, 0);

			if(cell != null) {
				for(String sY : StringTools.numericTokenize(cell)) {
					int y = Integer.parseInt(sY);

					// ? y == 西暦
					if(IntTools.isRange(y, 1900, 9999)) {
						_minY = Math.min(_minY, y);
						_maxY = Math.max(_maxY, y);
					}
				}
			}
		}

		for(int rr = 2; rr < table.getHeight(); rr++) {
			for(int cc = 0; cc < table.getWidth(); cc += 2) {
				String name = table.get(cc + 0, rr);
				String date = table.get(cc + 1, rr);

				if(name != null && date != null && StringTools.formatIs("9/9/9", date, true)) {
					int iDate = Integer.parseInt(TimeData.fromString(date).getString("YMD"));

					_map.put(iDate, name);
				}
			}
		}
	}

	public int getMinYear() {
		return _minY;
	}

	public int getMaxYear() {
		return _maxY;
	}

	public boolean isPublicHoliday(int date) {
		return getPublicHolidayName(date) != null;
	}

	/**
	 *
	 * @param date YYYYMMDD 形式, ex. 20170228
	 * @return
	 */
	public String getPublicHolidayName(int date) {
		return _map.get(date);
	}
}
