package charlotteFx.chart;

import charlotte.tools.DateToDay;
import charlotte.tools.RingList;

public class Chart {
	private String _currPair;
	private static final int CACHED_DAYS = 50;
	private RingList<ChartFile> _files = new RingList<ChartFile>(CACHED_DAYS);

	public Chart(String currPair) {
		_currPair = currPair;
		reload(DateToDay.getDay());
	}

	private void reload(int day) {
		for(int index = 0; index < CACHED_DAYS; index++) {
			_files.set(index, new ChartFile(_currPair, day - CACHED_DAYS / 2 + index));
		}
	}

	public ChartFile getFile(int day) {
		if(day <= _files.head().getDay() - CACHED_DAYS ||
				_files.tail().getDay() + CACHED_DAYS <= day
				) {
			reload(day);
		}
		while(day < _files.head().getDay()) {
			_files.shift(new ChartFile(_currPair, _files.head().getDay() - 1));
		}
		while(_files.tail().getDay() < day) {
			_files.add(new ChartFile(_currPair, _files.tail().getDay() + 1));
		}
		int index = day - _files.head().getDay();
		ChartFile file = _files.get(index);

		if(file.getDay() != day) {
			throw null;
		}
		return file;
	}

	public PriceData getPrice(long sec) {
		return getFile((int)(sec / 86400)).getPrice(sec);
	}

	public double getAsk(long sec) {
		return getPrice(sec).getAsk();
	}

	public double getBid(long sec) {
		return getPrice(sec).getBid();
	}

	public double getMid(long sec) {
		return getPrice(sec).getMid();
	}
}
