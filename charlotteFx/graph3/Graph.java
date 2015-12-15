package charlotteFx.graph3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.DateTimeToSec;
import charlotte.tools.FileTools;
import charlotteFx.chart.ChartManager;
import charlotteFx.chart.FxTime;

public class Graph {
	private long _startFxTime;
	private long _endFxTime;
	private long _fxTimeStep;
	private List<IChart> _charts = new ArrayList<IChart>();

	public Graph(long startFxTime, long endFxTime, long fxTimeStep) {
		_startFxTime = startFxTime;
		_endFxTime = endFxTime;
		_fxTimeStep = fxTimeStep;

		_charts.add(new IChart(Color.RED) {
			@Override
			public double getValue(long fxTime) {
				return ChartManager.USDJPY.getAsk(FxTime.fxTimeToSec(fxTime));
			}
		});

		_charts.add(new IChart(Color.ORANGE) {
			@Override
			public double getValue(long fxTime) {
				return ChartManager.USDJPY.getMid(FxTime.fxTimeToSec(fxTime));
			}
		});

		_charts.add(new IChart(Color.BLUE) {
			@Override
			public double getValue(long fxTime) {
				return ChartManager.USDJPY.getBid(FxTime.fxTimeToSec(fxTime));
			}
		});
	}

	public void addChart(IChart chart) {
		_charts.add(chart);
	}

	private static final int Y_SIZE = 241;
	private static final int Y_STEP = 5;
	private double _lowestValue = 999.0;
	private double _hiestValue = 0.0;

	public void generate() throws Exception {
		String dirBase =
				"C:/temp/" +
				DateTimeToSec.toDateTime(FxTime.fxTimeToSec(_startFxTime)) +
				"_" +
				DateTimeToSec.toDateTime(FxTime.fxTimeToSec(_endFxTime)) +
				"_" +
				_fxTimeStep +
				"_" +
				_charts.size() +
				"_";

		String vDir = dirBase + "variable";
		String cDir = dirBase + "constant";

		FileTools.mkdir(vDir);
		FileTools.mkdir(cDir);

		double lowValue;
		double hiValue;

		for(long fxTime = _startFxTime; fxTime <= _endFxTime; fxTime += _fxTimeStep * Y_SIZE / 2) {
			lowValue = 999.0;
			hiValue = 0.0;

			for(IChart chart : _charts) {
				for(double value : getValues(chart, fxTime)) {
					lowValue = Math.min(lowValue, value);
					hiValue = Math.max(hiValue, value);
				}
			}
			generateFile(vDir, fxTime, lowValue, hiValue);

			_lowestValue = Math.min(_lowestValue, lowValue);
			_hiestValue = Math.max(_hiestValue, hiValue);
		}
		for(long fxTime = _startFxTime; fxTime <= _endFxTime; fxTime += _fxTimeStep * Y_SIZE / 2) {
			generateFile(cDir, fxTime, _lowestValue, _hiestValue);
		}
	}

	private void generateFile(String dir, long fxTime, double lowValue, double hiValue) throws Exception {
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
			double midValue = (lowValue + hiValue) / 2.0;

			canvas.drawDouble(1, 47, 1, Color.BLACK, "" + hiValue);
			canvas.drawDouble(1, 447, 1, Color.BLACK, "" + midValue);
			canvas.drawDouble(1, 847, 1, Color.BLACK, "" + lowValue);
		}

		{
			String dt0 = "" + DateTimeToSec.toDateTime(FxTime.fxTimeToSec(fxTime));
			String dt1 = "" + DateTimeToSec.toDateTime(FxTime.fxTimeToSec(fxTime + _fxTimeStep * Y_SIZE / 4));
			String dt2 = "" + DateTimeToSec.toDateTime(FxTime.fxTimeToSec(fxTime + _fxTimeStep * Y_SIZE / 2));
			String dt3 = "" + DateTimeToSec.toDateTime(FxTime.fxTimeToSec(fxTime + _fxTimeStep * Y_SIZE * 3 / 4));

			canvas.drawDouble(51, 893, 1, Color.BLACK, dt0);
			canvas.drawDouble(351, 893, 1, Color.BLACK, dt1);
			canvas.drawDouble(651, 893, 1, Color.BLACK, dt2);
			canvas.drawDouble(951, 893, 1, Color.BLACK, dt3);
		}

		for(IChart chart : _charts) {
			double[] values = getValues(chart, fxTime);

			for(int c = 0; c + 1 < Y_SIZE; c++) {
				int x1 = 50 + c * Y_STEP;
				int x2 = 50 + (c + 1) * Y_STEP;
				int y1 = getY(values[c + 0], lowValue, hiValue);
				int y2 = getY(values[c + 1], lowValue, hiValue);

				canvas.drawLine(x1, y1, x2, y2, chart.getColor());
			}
		}

		{
			String file =
					dir +
					"/" +
					DateTimeToSec.toDateTime(FxTime.fxTimeToSec(fxTime)) +
					".png";

			bmp.writeToFile(file);
		}
	}

	private int getY(double value, double lowValue, double hiValue) {
		return (int)(850.0 - 800.0 * (value - lowValue) / (hiValue - lowValue));
	}

	private double[] getValues(IChart chart, long fxTime) {
		double[] ret = new double[Y_SIZE];

		for(int c = 0; c < Y_SIZE; c++) {
			double sum = 0.0;

			for(int d = 0; d < _fxTimeStep; d++) {
				sum += chart.getValue(fxTime);
				fxTime++;
			}
			ret[c] = sum / _fxTimeStep;
		}
		return ret;
	}
}
