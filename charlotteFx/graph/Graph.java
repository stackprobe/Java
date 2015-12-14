package charlotteFx.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.DateTimeToSec;

public class Graph {
	private static final int Y_SIZE = 241;
	private static final int Y_STEP = 5;
	private long _startSec;
	private long _secStep;
	private List<IChart> _charts = new ArrayList<IChart>();
	private double _lowValue = 110.0;
	private double _hiValue = 130.0;

	public Graph(long startSec, long secStep) {
		_startSec = startSec;
		_secStep = secStep;
	}

	public void add(IChart chart) {
		_charts.add(chart);
	}

	public void setLowHiValue(double lowValue, double hiValue) {
		_lowValue = lowValue;
		_hiValue = hiValue;
	}

	public void autoSetLowHiValue() {
		_lowValue = 999.0;
		_hiValue = 0.0;

		for(IChart chart : _charts) {
			for(int c = 0; c < Y_SIZE; c++) {
				long sec = _startSec + c * _secStep;
				double value = getValue(chart, sec);

				_lowValue = Math.min(_lowValue, value);
				_hiValue = Math.max(_hiValue, value);
			}
		}
	}

	private double getValue(IChart chart, long sec) {
		double sum = 0.0;

		for(int c = 0; c < _secStep; c++) {
			sum += chart.getValue(sec + c);
		}
		return sum / _secStep;
	}

	public Bmp getBmp() {
		Bmp bmp = new Bmp(1250, 900, Color.WHITE);
		Canvas canvas = new Canvas(bmp);

		{
			final int L = 50;
			final int T = 0;
			final int[] W = new int[] { 300, 300, 300, 300 };
			final int[] H = new int[] { 50, 400, 400, 50 };
			final Color color1 = new Color(245, 245, 245);
			final Color color2 = new Color(235, 235, 235);

			int l = L;

			for(int x = 0; x < W.length; x++) {
				int t = T;

				for(int y = 0; y < H.length; y++) {
					canvas.fillRect(l, t, W[x], H[y], (x + y) % 2 == 0 ? color1 : color2);
					t += H[y];
				}
				l += W[x];
			}
		}

		{
			double midPrice = (_lowValue + _hiValue) / 2.0;

			canvas.drawDouble(1, 47, 1, Color.BLACK, "" + _hiValue);
			canvas.drawDouble(1, 447, 1, Color.BLACK, "" + midPrice);
			canvas.drawDouble(1, 847, 1, Color.BLACK, "" + _lowValue);
		}

		{
			String dt0 = "" + DateTimeToSec.toDateTime(_startSec);
			String dt1 = "" + DateTimeToSec.toDateTime(_startSec + _secStep * Y_SIZE / 4);
			String dt2 = "" + DateTimeToSec.toDateTime(_startSec + _secStep * Y_SIZE / 2);
			String dt3 = "" + DateTimeToSec.toDateTime(_startSec + _secStep * Y_SIZE * 3 / 4);

			canvas.drawDouble(51, 893, 1, Color.BLACK, dt0);
			canvas.drawDouble(351, 893, 1, Color.BLACK, dt1);
			canvas.drawDouble(651, 893, 1, Color.BLACK, dt2);
			canvas.drawDouble(951, 893, 1, Color.BLACK, dt3);
		}

		for(IChart chart : _charts) {
			for(int c = 0; c + 1 < Y_SIZE; c++) {
				int x1 = 50 + c * Y_STEP;
				int x2 = 50 + (c + 1) * Y_STEP;
				long sec1 = _startSec + c * _secStep;
				long sec2 = _startSec + (c + 1) * _secStep;
				double value1 = getValue(chart, sec1);
				double value2 = getValue(chart, sec2);
				int y1 = getY(value1);
				int y2 = getY(value2);

				canvas.drawLine(x1, y1, x2, y2, chart.getColor());
			}
		}

		return bmp;
	}

	private int getY(double value) {
		return (int)(850.0 - 800.0 * (value - _lowValue) / (_hiValue - _lowValue));
	}
}
