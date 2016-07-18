package evergarden.fxchart.chart.test;

import charlotte.tools.DateTimeToSec;
import evergarden.fxchart.chart.Chart;
import evergarden.fxchart.chart.ChartManager;

public class Test01 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		Chart chart = ChartManager.USDJPY;

		for(long sec = DateTimeToSec.toSec(20151201000000L); sec < DateTimeToSec.toSec(20151202120000L); sec++) {
			System.out.println(DateTimeToSec.toDateTime(sec) + ": " + chart.getAsk(sec) + ", " + chart.getBid(sec));
		}
	}
}
