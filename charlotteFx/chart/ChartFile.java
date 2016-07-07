package charlotteFx.chart;

import charlotte.tools.AutoTable;
import charlotte.tools.CsvData;
import charlotte.tools.DateTimeToSec;
import charlotte.tools.DateToDay;
import charlotte.tools.FileTools;
import charlotte.tools.SortedList;
import charlotte.tools.SubList;

public class ChartFile {
	private String _currPair;
	private int _day;
	private SortedList<PriceData> _prices = new SortedList<PriceData>(PriceData.comp);

	public ChartFile(String currPair, int day) {
		_currPair = currPair;
		_day = day;
		loadPrices();
	}

	private void loadPrices() {
		try {
			String file = FileTools.combine("C:/var/Fx", DateToDay.toDate(_day) + "_" + _currPair + ".csv");

			if(FileTools.exists(file) == false) {
				throw new Exception("File not found: " + file);
			}
			CsvData csv = new CsvData();
			csv.readFile(file);
			AutoTable<String> table = csv.getTable();

			for(int r = 0; r < table.getHeight(); r++) {
				int c = 0;
				String sDateTime = table.get(c++, r);
				String sAsk = table.get(c++, r);
				String sBid = table.get(c++, r);

				long dateTime = Long.parseLong(sDateTime);
				double ask = Double.parseDouble(sAsk);
				double bid = Double.parseDouble(sBid);
				long sec = DateTimeToSec.toSec(dateTime);

				_prices.add(new PriceData(sec, ask, bid));
			}

			for(int index = 0; index + 1 < _prices.size(); index++) {
				PriceData p1 = _prices.get(index);
				PriceData p2 = _prices.get(index + 1);

				// 同じ時刻(有り得ないはず!)
				// -> どっちか削除する。
				if(p1.getSec() == p2.getSec()) {
					System.err.println("Same dateTime: " + DateTimeToSec.toDateTime(p1.getSec()));
					_prices.remove(index);
					index--;
					continue;
				}

				// 3sec以上空いている。
				// -> 直前のPriceの取得時刻が信用出来ない。
				// -> その前が取得できていれば、削除する。
				if(p1.getSec() + 3 <= p2.getSec() &&
						1 <= index &&
						p1.getSec() - 2 <= _prices.get(index - 1).getSec()
						) {
					_prices.remove(index);
					index--;
					continue;
				}
			}
		}
		catch(Throwable e) {
			System.out.println("Failed loadPrices(): " + e.getMessage());
		}
	}

	public int getDay() {
		return _day;
	}

	public PriceData getPrice(long sec) {
		SubList<PriceData> ret = _prices.getMatchWithEdge(PriceData.ferret(sec));

		if(ret.size() == 3) {
			return ret.get(1);
		}
		return PriceData.createBetween(sec, ret.get(0), ret.get(1));
	}
}
