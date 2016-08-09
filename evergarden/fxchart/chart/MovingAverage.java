package evergarden.fxchart.chart;

import charlotte.tools.DateTimeToSec;

public class MovingAverage {
	private Chart _chart;
	private long _bound;
	private long _fxTime;
	private double _total;

	public MovingAverage(Chart chart, long bound) {
		if(bound < 1L) {
			throw new IllegalArgumentException();
		}
		_chart = chart;
		_bound = bound;
		reload(DateTimeToSec.Now.getSec());
	}

	private void reload(long fxTime) {
		_fxTime = fxTime;
		_total = 0L;

		for(long c = 0; c < _bound; c++) {
			_total += _chart.getMid(FxTime.fxTimeToSec(fxTime));
			fxTime--;
		}
	}

	public void move(long fxTime) {
		if(fxTime <= _fxTime - _bound ||
				_fxTime + _bound <= fxTime
				) {
			reload(fxTime);
			return;
		}
		while(fxTime < _fxTime) {
			_total -= _chart.getMid(FxTime.fxTimeToSec(_fxTime));
			_total += _chart.getMid(FxTime.fxTimeToSec(_fxTime - _bound));
			_fxTime--;
		}
		while(_fxTime < fxTime) {
			_fxTime++;
			_total -= _chart.getMid(FxTime.fxTimeToSec(_fxTime - _bound));
			_total += _chart.getMid(FxTime.fxTimeToSec(_fxTime));
		}
	}

	public double getMid() {
		return _total / _bound;
	}
}
