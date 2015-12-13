package charlotteFx.graph.test;

import java.awt.Color;

import charlotte.tools.DateTimeToSec;
import charlotteFx.chart.ChartManager;
import charlotteFx.graph.Graph;
import charlotteFx.graph.IChart;

public class Test01 {
	public static void main(String[] args) {
		try {
			test01();
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
		test01_a(20150801000000L, 60L + 15);
		test01_a(20150801000000L, 3600L);
		test01_a(20150801000000L, 86400L);
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
		g.getBmp().writeToFile("C:/temp/Test01_" + dateTime + "_" + secSpan + ".png");
	}
}
