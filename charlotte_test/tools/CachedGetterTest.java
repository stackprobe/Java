package charlotte_test.tools;

import charlotte.tools.CachedGetter;
import charlotte.tools.ParamedGetter;

public class CachedGetterTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		ParamedGetter<String, String> pg = CachedGetter.create(new ParamedGetter<String, String>() {
			@Override
			public String get(String key) throws Exception {
				System.out.println("CALLED " + key);
				return "[" + key + "]";
			}
		});

		System.out.println(pg.get("A"));
		System.out.println(pg.get("B"));
		System.out.println(pg.get("C"));
		System.out.println(pg.get("A"));
		System.out.println(pg.get("B"));
		System.out.println(pg.get("01"));
		System.out.println(pg.get("02"));
		System.out.println(pg.get("03"));
		System.out.println(pg.get("B"));
		System.out.println(pg.get("C"));
		System.out.println(pg.get("01"));
		System.out.println(pg.get("02"));
		System.out.println(pg.get("03"));
	}
}
