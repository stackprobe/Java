package charlotte.tools;

import java.util.List;

public class EraDate {
	private static final String ERA_ALPHAS = "MTSH";

	private static final String[] ERA_NAMES = new String[] {
			"明治",
			"大正",
			"昭和",
			"平成",
	};

	private static final int[] ERA_FIRST_DATES = new int[] {
			18680125,
			19120730,
			19261225,
			19890108,
	};

	private static int getEraEnd() {
		return ERA_FIRST_DATES.length;
	}

	private int _e;
	private int _n;
	private int _m;
	private int _d;

	public EraDate(int e, int n, int m, int d) {
		_e = e;
		_n = n;
		_m = m;
		_d = d;
	}

	public EraDate(int y, int m, int d) {
		set(y, m, d);
	}

	public EraDate(TimeData td) {
		int[] c = td.getC();

		int y = c[0];
		int m = c[1];
		int d = c[2];

		set(y, m, d);
	}

	public EraDate(String str) {
		try {
			str = str.replace("元", "1");

			List<String> tokens = StringTools.numericTokenize(str);
			int y = Integer.parseInt(tokens.get(0));
			int m = Integer.parseInt(tokens.get(1));
			int d = Integer.parseInt(tokens.get(2));

			if(y < 1000) {
				_e = getE(str);
				_n = y;
				_m = m;
				_d = d;
			}
			else {
				set(y, m, d);
			}
		}
		catch(Throwable e) {
			// ignore

			_e = 0;
			_n = 0;
			_m = 0;
			_d = 0;
		}
	}

	private void set(int y, int m, int d) {
		int date = y * 10000 + m * 100 + d;

		for(_e = getEraEnd() - 1; 0 <= _e; _e--) {
			if(ERA_FIRST_DATES[_e] <= date) {
				_n = y + 1 - ERA_FIRST_DATES[_e] / 10000;
				_m = m;
				_d = d;
				return;
			}
		}
		_e = 0;
		_n = 0;
		_m = 0;
		_d = 0;
	}

	public int getE(String str) {
		int e;

		str = str.toUpperCase();

		for(e = 0; e < getEraEnd() - 1; e++) {
			if(str.indexOf(ERA_ALPHAS.charAt(e)) != -1 || str.indexOf(ERA_NAMES[e]) != -1) {
				break;
			}
		}
		return e; // if not found -> now
	}

	@Override
	public String toString() {
		return toString("EN年M月D日");
	}

	public String toString(String format) {
		String e = ERA_NAMES[_e];
		String n;
		String m = String.format("%02d", _m);
		String d = String.format("%02d", _d);

		if(_n == 1) {
			n = "元";
		}
		else {
			n = "" + _n;
		}
		String str = format;

		str = str.replace("E", e);
		str = str.replace("N", n);
		str = str.replace("M", m);
		str = str.replace("D", d);

		return str;
	}
}
