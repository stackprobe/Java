package evergarden.fxchart.graph2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import evergarden.fxchart.chart.Chart;
import evergarden.fxchart.chart.FxTime;
import evergarden.fxchart.chart.MovingAverage;

public class Chart2 {
	private Chart _chart;
	private List<ChartLine> _chartLines = new ArrayList<ChartLine>();

	public Chart2(Chart chart) {
		_chart = chart;
		makeChartLines();
	}

	private void makeChartLines() {
		_chartLines.add(new ChartLine(Color.RED) {
			@Override
			public double getValue(long fxTime) {
				return _chart.getAsk(FxTime.fxTimeToSec(fxTime));
			}
		});

		_chartLines.add(new ChartLine(Color.ORANGE) {
			@Override
			public double getValue(long fxTime) {
				return _chart.getMid(FxTime.fxTimeToSec(fxTime));
			}
		});

		_chartLines.add(new ChartLine(Color.BLUE) {
			@Override
			public double getValue(long fxTime) {
				return _chart.getBid(FxTime.fxTimeToSec(fxTime));
			}
		});

		_chartLines.add(new ChartLine(new Color(0, 255, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 60L * 15);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(0, 240, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 60L * 30);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(0, 220, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 60L * 45);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(10, 200, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 3600L);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(20, 200, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 3600L * 2);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(30, 200, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 3600L * 3);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(60, 200, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 3600L * 6);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(120, 200, 0)) {
			private MovingAverage _ma = new MovingAverage(_chart, 3600L * 12);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(240, 200, 10)) {
			private MovingAverage _ma = new MovingAverage(_chart, 86400L);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(240, 200, 20)) {
			private MovingAverage _ma = new MovingAverage(_chart, 86400L * 2);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(240, 200, 30)) {
			private MovingAverage _ma = new MovingAverage(_chart, 86400L * 3);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});

		_chartLines.add(new ChartLine(new Color(240, 200, 40)) {
			private MovingAverage _ma = new MovingAverage(_chart, 86400L * 4);

			@Override
			public double getValue(long fxTime) {
				_ma.move(fxTime);
				return _ma.getMid();
			}
		});
	}

	public List<ChartLine> getChartLines() {
		return _chartLines;
	}
}
