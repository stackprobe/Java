package charlotteFx.graph3.test;

import java.awt.Color;

import charlotte.tools.DateTimeToSec;
import charlotteFx.chart.ChartManager;
import charlotteFx.chart.FxTime;
import charlotteFx.chart.PriceData;
import charlotteFx.graph3.Graph;
import charlotteFx.graph3.MaChart;

public class Test01 {
	public static void main(String[] args) {
		try {
			{
				long sec = DateTimeToSec.getSec();
				sec /= 86400L;
				sec *= 86400L;
				sec--;
				PriceData.DEF_PRICE = ChartManager.USDJPY.getMid(sec);
			}

			//test01();
			//test02();
			test03();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		long startFxTime = FxTime.secToFxTime(DateTimeToSec.toSec(20150901000000L));
		long endFxTime;

		{
			long sec = DateTimeToSec.getSec();
			sec /= 86400L;
			sec *= 86400L;
			endFxTime = FxTime.secToFxTime(sec);
		}

		long fxTimeStep = 3600L;

		{
			Graph g = new Graph(startFxTime, endFxTime, fxTimeStep);
			g.generate();
		}
	}

	private static void test02() throws Exception {
		long startFxTime;
		long endFxTime;

		{
			long sec = DateTimeToSec.getSec();
			sec /= 86400L;
			sec *= 86400L;
			endFxTime = FxTime.secToFxTime(sec);
		}

		startFxTime = endFxTime - 86400L * 7;

		long fxTimeStep = 60L;

		{
			Graph g = new Graph(startFxTime, endFxTime, fxTimeStep);
			g.generate();
		}
	}

	private static void test03() throws Exception {
		long endFxTime;

		{
			long sec = DateTimeToSec.getSec();
			sec /= 86400L;
			sec *= 86400L;
			endFxTime = FxTime.secToFxTime(sec);
		}

		for(int d = 5; d <= 30; d += 5) {
			for(long s : new long[] { 60L, 900L, 3600L, 86400L }) {
				for(int c = 1; c <= 2; c++) {
					long startFxTime = endFxTime - 86400L * d;
					long fxTimeStep = s;

					{
						Graph g = new Graph(startFxTime, endFxTime, fxTimeStep);

						if(c == 2) {
							g.addChart(new MaChart(new Color(0, 255, 0), 60L * 15));
							g.addChart(new MaChart(new Color(0, 225, 0), 60L * 30));
							g.addChart(new MaChart(new Color(0, 195, 0), 60L * 45));
							g.addChart(new MaChart(new Color(0, 165, 0), 3600L));
							g.addChart(new MaChart(new Color( 50, 165, 0), 3600L * 2));
							g.addChart(new MaChart(new Color(100, 165, 0), 3600L * 3));
							g.addChart(new MaChart(new Color(150, 165, 0), 3600L * 4));
							g.addChart(new MaChart(new Color(200, 165,  50), 86400L));
							g.addChart(new MaChart(new Color(200, 165, 100), 86400L * 2));
							g.addChart(new MaChart(new Color(200, 165, 150), 86400L * 3));
							g.addChart(new MaChart(new Color(200, 165, 200), 86400L * 4));
						}
						g.generate();
					}
				}
			}
		}
	}
}
