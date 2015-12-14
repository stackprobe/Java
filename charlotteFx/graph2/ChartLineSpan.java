package charlotteFx.graph2;

/**
 * 範囲: _startFxTime(含む)～_endFxTime(含まない)
 *
 */
public class ChartLineSpan {
	private ChartLine _chartLine;
	private long _startFxTime;
	private long _endFxTime;

	public ChartLineSpan(ChartLine chartLine, long startFxTime, long endFxTime) {
		_chartLine = chartLine;
		_startFxTime = startFxTime;
		_endFxTime = endFxTime;
	}

	public double getAvg() {
		double sum = 0.0;

		for(long fxTime = _startFxTime; fxTime < _endFxTime; fxTime++) {
			sum += _chartLine.getValue(fxTime);
		}
		return sum / (_endFxTime - _startFxTime);
	}

	public double getMin() {
		double ret = 999.0;

		for(long fxTime = _startFxTime; fxTime < _endFxTime; fxTime++) {
			ret = Math.min(ret, _chartLine.getValue(fxTime));
		}
		return ret;
	}

	public double getMax() {
		double ret = 0.0;

		for(long fxTime = _startFxTime; fxTime < _endFxTime; fxTime++) {
			ret = Math.max(ret, _chartLine.getValue(fxTime));
		}
		return ret;
	}
}
