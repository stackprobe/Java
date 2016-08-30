package evergarden.violet;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import charlotte.satellite.WinAPITools;

public class Test01 {
	public static void main(String[] args) {
		try {
			System.out.println("TMP: " + WinAPITools.i().getEnv("TMP", "error"));

			System.out.println(System.getProperty("java.version"));

			{
				{
					Calendar cal = Calendar.getInstance();

					int y = cal.get(Calendar.YEAR);
					int m = cal.get(Calendar.MONTH) + 1;
					int d = cal.get(Calendar.DAY_OF_MONTH);

					System.out.println(y + "/" + m + "/" + d);
				}

				Locale.setDefault(new Locale("ja", "JP", "JP"));

				{
					Calendar cal = Calendar.getInstance();

					int y = cal.get(Calendar.YEAR); // 和暦年が返る!!!
					int m = cal.get(Calendar.MONTH) + 1;
					int d = cal.get(Calendar.DAY_OF_MONTH);

					System.out.println(y + "/" + m + "/" + d);

					Date dt = cal.getTime();

					y = dt.getYear() + 1900;
					m = dt.getMonth() + 1;
					d = dt.getDate();

					System.out.println(y + "/" + m + "/" + d); // こうすれば西暦
				}
			}

			{
				String string = "ABC";

				System.out.println("" + (string == null));
				System.out.println("" + (string.equals(null)));
			}

			{
				String string = null;

				System.out.println("" + (string == null));
				System.out.println("" + (string.equals(null)));
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}