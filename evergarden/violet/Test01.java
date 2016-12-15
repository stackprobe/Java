package evergarden.violet;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import charlotte.satellite.WinAPITools;
import charlotte.tools.ReflecTools;
import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;
import charlotte.tools.XNode;

public class Test01 {
	public static void main(String[] args) {
		try {
			System.out.println("TMP: " + WinAPITools.i().getEnv("TMP", "error"));
			System.out.println("tmp: " + System.getProperty("java.io.tmpdir"));

			System.out.println("JAVA_VERSION: " + System.getProperty("java.version"));

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
				//System.out.println("" + (string.equals(null))); // NullPointerException
			}

			System.out.println(SystemTools.getHostName());
			System.out.println(SystemTools.getHostIP());

			// ----

			//System.out.println(new File((String)null).getCanonicalPath()); // NullPointerException

			System.out.println(new File("").getCanonicalPath());   // -> C:\pleiades\workspace\Test02
			System.out.println(new File(".").getCanonicalPath());  // -> C:\pleiades\workspace\Test02
			System.out.println(new File("..").getCanonicalPath()); // -> C:\pleiades\workspace

			System.out.println(new File("a").getCanonicalPath());     // -> C:\pleiades\workspace\Test02\a
			System.out.println(new File("a/.").getCanonicalPath());   // -> C:\pleiades\workspace\Test02\a
			System.out.println(new File("./a").getCanonicalPath());   // -> C:\pleiades\workspace\Test02\a
			System.out.println(new File("./a/.").getCanonicalPath()); // -> C:\pleiades\workspace\Test02\a

			System.out.println(new File("").exists()); // false
			System.out.println(new File("").isDirectory()); // false

			System.out.println(new File(new File("").getCanonicalPath()).exists()); // true
			System.out.println(new File(new File("").getCanonicalPath()).isDirectory()); // true

			// ----

			//System.out.println(Class.forName("String")); // ClassNotFoundException
			System.out.println(Class.forName("java.lang.String"));
			//System.out.println(Class.forName("XXX---XXX")); // ClassNotFoundException

			// ----

			System.out.println(StringTools.PUNCT);
			System.out.println(StringTools.HAN_KATAKANA);

			// ----

			System.out.println("i");
			System.out.println(int.class.equals(ReflecTools.getField(O.class, "i").getType())); // true
			System.out.println(int.class.equals(ReflecTools.getField(O.class, "l").getType())); // false
			System.out.println(int.class.equals(ReflecTools.getField(O.class, "d").getType())); // false
			System.out.println(int.class.equals(ReflecTools.getField(O.class, "s").getType())); // false

			System.out.println("l");
			System.out.println(long.class.equals(ReflecTools.getField(O.class, "i").getType())); // false
			System.out.println(long.class.equals(ReflecTools.getField(O.class, "l").getType())); // true
			System.out.println(long.class.equals(ReflecTools.getField(O.class, "d").getType())); // false
			System.out.println(long.class.equals(ReflecTools.getField(O.class, "s").getType())); // false

			System.out.println("d");
			System.out.println(double.class.equals(ReflecTools.getField(O.class, "i").getType())); // false
			System.out.println(double.class.equals(ReflecTools.getField(O.class, "l").getType())); // false
			System.out.println(double.class.equals(ReflecTools.getField(O.class, "d").getType())); // true
			System.out.println(double.class.equals(ReflecTools.getField(O.class, "s").getType())); // false

			System.out.println("s");
			System.out.println(String.class.equals(ReflecTools.getField(O.class, "i").getType())); // false
			System.out.println(String.class.equals(ReflecTools.getField(O.class, "l").getType())); // false
			System.out.println(String.class.equals(ReflecTools.getField(O.class, "d").getType())); // false
			System.out.println(String.class.equals(ReflecTools.getField(O.class, "s").getType())); // true

			System.out.println("----");

			System.out.println(Object.class.equals(ReflecTools.getField(O.class, "s").getType())); // false

			System.out.println(List.class.equals(ReflecTools.getField(O.class, "ls").getType())); // true
			System.out.println(Map.class.equals(ReflecTools.getField(O.class, "mss").getType())); // true

			// ----

			{
				new XNode("a", "b").getNode("c");
				new XNode("a", "b").getNode("c", 123);
			}

			// ----

			System.out.println("" + ("1".split("[,]").length)); // 1
			System.out.println("" + (",".split("[,]").length)); // 0
			System.out.println("" + ("".split("[,]").length)); // 1

			System.out.println("" + (",,".split("[,]").length)); // 0
			System.out.println("" + (",,,".split("[,]").length)); // 0
			System.out.println("" + (",,,,".split("[,]").length)); // 0
			System.out.println("" + (",,1".split("[,]").length)); // 3
			System.out.println("" + (",,,1".split("[,]").length)); // 4
			System.out.println("" + (",,,,1".split("[,]").length)); // 5

			System.out.println("" + (",,,1,1,1".split("[,]").length)); // 6
			System.out.println("" + ("1,1,1,,,".split("[,]").length)); // 3
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static class O {
		public int i;
		public long l;
		public double d;
		public String s;
		public List<String> ls;
		public Map<String, String> mss;
	}
}
