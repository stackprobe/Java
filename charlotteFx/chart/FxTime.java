package charlotteFx.chart;

public class FxTime {
	private static final int START_TIME = 0 * 86400 + 7 * 3600 +  0 * 60; // 月曜日(0日後) 07:00:00
	private static final int END_TIME   = 5 * 86400 + 5 * 3600 + 50 * 60; // 土曜日(5日後) 05:50:00
	private static final int TIME_CYCLE = 7 * 86400; // 1週間

	private static final int TRADING_TIME = END_TIME - START_TIME; // 取引可能期間
	private static final int INTERVAL_TIME = TIME_CYCLE - TRADING_TIME; // お休み期間

	public static long secToFxTime(long sec) {
		long count = sec / TIME_CYCLE;
		long rem   = sec % TIME_CYCLE;

		if(rem < START_TIME) {
			rem = START_TIME;
		}
		else if(END_TIME < rem) {
			rem = END_TIME;
		}
		rem -= START_TIME;

		return count * TRADING_TIME + rem;
	}

	public static long fxTimeToSec(long fxTime) {
		long count = fxTime / TRADING_TIME;
		long rem   = fxTime % TRADING_TIME;

		rem += START_TIME;

		return count * TIME_CYCLE + rem;
	}
}
