package charlotteFx.chart;

import java.util.Comparator;

import charlotte.tools.LongTools;

public class PriceData {
	public static double DEF_PRICE = 120.0;
	private long _sec;
	private double _ask;
	private double _bid;

	public PriceData(long sec, double ask, double bid) {
		_sec = sec;
		_ask = ask;
		_bid = bid;
	}

	public long getSec() {
		return _sec;
	}

	public double getAsk() {
		return _ask;
	}

	public double getBid() {
		return _bid;
	}

	public double getMid() {
		return (_ask + _bid) / 2.0;
	}

	public static Comparator<PriceData> comp = new Comparator<PriceData>() {
		@Override
		public int compare(PriceData p1, PriceData p2) {
			return LongTools.comp.compare(p1._sec, p2._sec);
		}
	};

	public static PriceData ferret(long sec) {
		return new PriceData(sec, DEF_PRICE, DEF_PRICE);
	}

	public static PriceData createBetween(long sec, PriceData l, PriceData h) {
		if(l == null && h == null) {
			return new PriceData(sec, DEF_PRICE, DEF_PRICE);
		}
		if(l == null) {
			return h;
		}
		if(h == null) {
			return l;
		}
		return new PriceData(
				sec,
				getBetween(sec, l._sec, h._sec, l._ask, h._ask),
				getBetween(sec, l._sec, h._sec, l._bid, h._bid)
				);
	}

	private static double getBetween(long sec, long lSec, long hSec, double lVal, double hVal) {
		long numer = sec - lSec;
		long denom = hSec - lSec;
		double rate = (double)numer / denom;

		return lVal + rate * (hVal - lVal);
	}
}
