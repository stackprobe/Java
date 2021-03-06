package charlotte_test.tools;

import charlotte.tools.DateTimeToSec;

public class DateTimeToSecTest {
	public static void main(String[] args) {
		try {
			System.out.println("today=" + DateTimeToSec.Now.getDateTime());
			System.out.println("19000101000000=" + DateTimeToSec.toSec(19000101000000L));
			System.out.println("22000101000000=" + DateTimeToSec.toSec(22000101000000L));

			// 19000101000000-22000101000000
			/*
			for(long sec = 59926608000L; sec <= 69393715200L; sec += (long)(Math.random() * 86400.0)) {
				long dateTime = DateTimeToSec.toDateTime(sec);
				long rSec = DateTimeToSec.toSec(dateTime);

				System.out.println(sec + " -> " + dateTime + " -> " + rSec);

				if(sec != rSec) {
					throw new Exception("ng");
				}
			}
			*/

			System.out.println("6h_ago=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() - 3600 * 6));
			System.out.println("12h_ago=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() - 3600 * 12));
			System.out.println("18h_ago=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() - 3600 * 18));
			System.out.println("24h_ago=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() - 3600 * 24));

			System.out.println("6h_later=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + 3600 * 6));
			System.out.println("12h_later=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + 3600 * 12));
			System.out.println("18h_later=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + 3600 * 18));
			System.out.println("24h_later=" + DateTimeToSec.toDateTime(DateTimeToSec.Now.getSec() + 3600 * 24));

			System.out.println("10000101000000=" + DateTimeToSec.toSec(10000101000000L));
			System.out.println("99991231235959=" + DateTimeToSec.toSec(99991231235959L));

			//System.out.println("40000000000->" + DateTimeToSec.toDateTime(40000000000L));
			//System.out.println("90000000000->" + DateTimeToSec.toDateTime(90000000000L));
			//System.out.println("99999999999->" + DateTimeToSec.toDateTime(99999999999L));

			{
				long a = 63630399881L;
				long b = 63630641801L;

				long t = a + ((b - a) * 2) / 3;

				long epoch = DateTimeToSec.toSec(19700101000000L);

				//t += epoch; // posixじゃなかった...

				System.out.println(DateTimeToSec.toDateTime(t));
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
