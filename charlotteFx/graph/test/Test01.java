package charlotteFx.graph.test;

import java.awt.Color;

import charlotte.tools.DateTimeToSec;
import charlotte.tools.StringTools;
import charlotteFx.chart.ChartManager;
import charlotteFx.chart.FxTime;
import charlotteFx.chart.MovingAverage;
import charlotteFx.graph.Graph;
import charlotteFx.graph.IChart;

public class Test01 {
	public static void main(String[] args) {
		try {
			test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		test01_a(20151201000000L, 1L);
		test01_a(20151201000000L, 2L);
		test01_a(20151201000000L, 3L);
		test01_a(20151201000000L, 4L);

		test01_a(20150801000000L, 60L);
		test01_a(20150801000000L, 60L * 15);
		test01_a(20150801000000L, 60L * 30);
		test01_a(20150801000000L, 3600L);
		test01_a(20150801000000L, 3600L * 2);
		test01_a(20150801000000L, 3600L * 3);
		test01_a(20150801000000L, 3600L * 4);
		test01_a(20150701000000L, 86400L);
	}

	private static void test01_a(long dateTime, long secSpan) throws Exception {
		Graph g = new Graph(DateTimeToSec.toSec(dateTime), secSpan);

		g.add(new IChart() {
			@Override
			public Color getColor() {
				return Color.RED;
			}

			@Override
			public double getPrice(long sec) {
				return ChartManager.USDJPY.getAsk(sec);
			}
		});

		g.add(new IChart() {
			@Override
			public Color getColor() {
				return Color.ORANGE;
			}

			@Override
			public double getPrice(long sec) {
				return ChartManager.USDJPY.getMid(sec);
			}
		});

		g.add(new IChart() {
			@Override
			public Color getColor() {
				return Color.BLUE;
			}

			@Override
			public double getPrice(long sec) {
				return ChartManager.USDJPY.getBid(sec);
			}
		});

		g.autoSetLowHiPrice();
		g.getBmp().writeToFile("C:/temp/Test01_" + dateTime + "_" + StringTools.zPad("" + secSpan, 5) + ".png");
	}

	private static void test02() throws Exception {
		test02_a(20150801000000L, 1L);
		test02_a(20150801000000L, 2L);
		test02_a(20150801000000L, 3L);
		test02_a(20150801000000L, 4L);
		test02_a(20150801000000L, 60L);
		test02_a(20150801000000L, 60L * 2);
		test02_a(20150801000000L, 60L * 3);
		test02_a(20150801000000L, 60L * 4);
		test02_a(20150801000000L, 3600L);
		test02_a(20150801000000L, 3600L * 2);
		test02_a(20150801000000L, 3600L * 3);
		test02_a(20150801000000L, 3600L * 4);
	}

	private static void test02_a(long dateTime, long secSpan) throws Exception {
		Graph g = new Graph(DateTimeToSec.toSec(dateTime), secSpan);

		g.add(new IChart() {
			@Override
			public Color getColor() {
				return Color.RED;
			}

			@Override
			public double getPrice(long sec) {
				return ChartManager.USDJPY.getMid(sec);
			}
		});

		g.add(new IChart() {
			private MovingAverage _ma = new MovingAverage(ChartManager.USDJPY, 60L);

			@Override
			public Color getColor() {
				return Color.ORANGE;
			}

			@Override
			public double getPrice(long sec) {
				_ma.move(FxTime.secToFxTime(sec));
				return _ma.getMid();
			}
		});

		g.add(new IChart() {
			private MovingAverage _ma = new MovingAverage(ChartManager.USDJPY, 3600L);

			@Override
			public Color getColor() {
				return Color.MAGENTA;
			}

			@Override
			public double getPrice(long sec) {
				_ma.move(FxTime.secToFxTime(sec));
				return _ma.getMid();
			}
		});

		g.add(new IChart() {
			private MovingAverage _ma = new MovingAverage(ChartManager.USDJPY, 86400L);

			@Override
			public Color getColor() {
				return Color.GREEN;
			}

			@Override
			public double getPrice(long sec) {
				_ma.move(FxTime.secToFxTime(sec));
				return _ma.getMid();
			}
		});

		g.add(new IChart() {
			private MovingAverage _ma = new MovingAverage(ChartManager.USDJPY, 86400L * 25);

			@Override
			public Color getColor() {
				return Color.CYAN;
			}

			@Override
			public double getPrice(long sec) {
				_ma.move(FxTime.secToFxTime(sec));
				return _ma.getMid();
			}
		});

		g.autoSetLowHiPrice();
		g.getBmp().writeToFile("C:/temp/Test02_" + dateTime + "_" + StringTools.zPad("" + secSpan, 5) + ".png");
	}
}
