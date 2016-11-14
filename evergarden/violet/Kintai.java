package evergarden.violet;

import java.util.Comparator;

import charlotte.tools.DateTimeToSec;
import charlotte.tools.DateToDay;
import charlotte.tools.IntTools;
import charlotte.tools.LongTools;
import charlotte.tools.SortedList;
import charlotte.tools.SubList;
import charlotte.tools.TimeData;
import charlotte.tools.XNode;

public class Kintai {
	public static void main(String[] args) {
		try {
			//main2("C:/var/201609_kintai.xml");
			//main2("C:/var/tt_20161011.xml");
			//main2("C:/var/tt_20161014.xml");
			//main2("C:/var/tt_20161021.xml");
			//main2("C:/var/tt_20161028.xml");
			//main2("C:/var/tt_20161031.xml");
			main2("C:/var/tt_20161111.xml");

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static class DateTimeSpan {
		public long bgn;
		public long end;

		public DateTimeSpan(long bgn, long end) {
			this.bgn = bgn;
			this.end = end;
		}
	}

	private static void main2(String kintaiFile) throws Exception {
		XNode root = XNode.load(kintaiFile);

		SortedList<DateTimeSpan> dtss = new SortedList<DateTimeSpan>(new Comparator<DateTimeSpan>() {
			@Override
			public int compare(DateTimeSpan a, DateTimeSpan b) {
				int ret;

				ret = LongTools.comp.compare(a.bgn, b.bgn);
				if(ret != 0) {
					return ret;
				}

				/*
				ret = LongTools.comp.compare(a.end, b.end);
				if(ret != 0) {
					return ret;
				}
				*/

				return 0;
			}
		});

		int minDate = Integer.MAX_VALUE;
		int maxDate = 0;

		for(XNode node : root.getNodes("TimeSheet/diffgram/TimeSheetSet/ActualTime")) {
			TimeData date = TimeData.fromISO8061(node.getNodeValue("WorkDate"));
			TimeData bgnTime = TimeData.fromISO8061(node.getNodeValue("StartTime"));
			TimeData endTime = TimeData.fromISO8061(node.getNodeValue("FinishTime"));
			bgnTime = new TimeData(bgnTime.getTime() - 9 * 3600L); // -= JST
			endTime = new TimeData(endTime.getTime() - 9 * 3600L); // -= JST
			int d = Integer.parseInt(date.getString("YMD"));
			//long bgn = Long.parseLong(date.getString("YMD") + bgnTime.getString("hms"));
			//long end = Long.parseLong(date.getString("YMD") + endTime.getString("hms"));
			long bgn = Long.parseLong(bgnTime.getString("YMDhms"));
			long end = Long.parseLong(endTime.getString("YMDhms"));
			DateTimeSpan dts = new DateTimeSpan(bgn, end);

			dtss.add(dts);

			minDate = Math.min(minDate, d);
			maxDate = Math.max(maxDate, d);
		}

		for(int date = minDate; date <= maxDate; date = DateToDay.toDate(DateToDay.toDay(date) + 1)) {
			SubList<DateTimeSpan> spans = dtss.getMatch(new DateTimeSpan(date * 1000000L, 0L), new Comparator<DateTimeSpan>() {
				@Override
				public int compare(DateTimeSpan a, DateTimeSpan b) {
					int ad = (int)(a.bgn / 1000000L);
					int bd = (int)(b.bgn / 1000000L);

					return IntTools.comp.compare(ad, bd);
				}
			});

			{
				int tmp = date;
				int d = tmp % 100;
				tmp /= 100;
				int m = tmp % 100;
				int y = tmp / 100;

				System.out.print(y + "/" + m + "/" + d + " ");
			}

			if(1 <= spans.size()) {
				int yasumiMin = 0;

				for(int index = 1; index < spans.size(); index++) {
					DateTimeSpan a = spans.get(index - 1);
					DateTimeSpan b = spans.get(index);

					long diff = DateTimeToSec.toSec(b.bgn) - DateTimeToSec.toSec(a.end);

					yasumiMin += (int)(diff / 60L);
				}
				int bgnHm = (int)(spans.get(0).bgn % 1000000L) / 100;
				int endHm = (int)(spans.get(spans.size() - 1).end % 1000000L) / 100;

				System.out.println(bgnHm + " " + endHm + " " + yasumiMin);
			}
			else {
				System.out.println("-");
			}
		}
	}
}
