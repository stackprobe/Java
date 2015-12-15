package charlotteFx.graph3;

import java.awt.Color;

import charlotteFx.chart.ChartManager;
import charlotteFx.chart.MovingAverage;

public class MaChart extends IChart {
	private MovingAverage _ma;

	public MaChart(Color color, long bound) {
		super(color);
		_ma = new MovingAverage(ChartManager.USDJPY, bound);
	}

	@Override
	public double getValue(long fxTime) {
		_ma.move(fxTime);
		return _ma.getMid();
	}
}
