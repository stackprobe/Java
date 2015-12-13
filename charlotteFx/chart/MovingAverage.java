package charlotteFx.chart;

import charlotte.tools.DateTimeToSec;

public class MovingAverage {
	private Chart _chart;
	private long _bound;
	private long _sec;
	private double _total;

	public MovingAverage(Chart chart, long bound) {
		if(bound < 1L) {
			throw new IllegalArgumentException();
		}
		_chart = chart;
		_bound = bound;
		reload(DateTimeToSec.getSec());
	}

	private void reload(long sec) {
		_sec = sec;
		_total = 0L;

		for(long c = 0; c < _bound; c++) {
			_total += _chart.getMid(sec);
			sec--;
		}
	}

	public void move(long sec) {
		if(sec <= _sec - _bound ||
				_sec + _bound <= sec
				) {
			reload(sec);
			return;
		}
		while(sec < _sec) {
			_total -= _chart.getMid(_sec);
			_total += _chart.getMid(_sec - _bound);
			_sec--;
		}
		while(_sec < sec) {
			_sec++;
			_total -= _chart.getMid(_sec - _bound);
			_total += _chart.getMid(_sec);
		}
	}

	public double getMid() {
		return _total / _bound;
	}
}
