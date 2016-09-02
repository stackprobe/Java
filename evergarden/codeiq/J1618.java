package evergarden.codeiq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.IntTools;
import charlotte.tools.MathTools;

public class J1618 {
	public static void main(String[] args) {
		try {
			test01("15:5:8:4:10:3:2".split("[:]"), 2);
			test01("35:10:13:12:17:10:4:18:3:11:5:7".split("[:]"), 6);

			// ----

			test01("6:3:1:2:3".split("[:]"), 1);
			test01("6:3:3:2:1".split("[:]"), 1);
			test01("6:3:1:2:4".split("[:]"), 0);
			test01("12:5:1:2:3:4:5".split("[:]"), 1);

			//test03_a();

			for(int l = 10000; l <= 15000; l += 1000) {
				test04(l, 0.5, 1, 9000);
				test04(l, 0.9, 1, 5000);
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01(String[] lines, int assume) throws Exception {
		int l = Integer.parseInt(lines[0]);
		int n = Integer.parseInt(lines[1]);
		int[] bars = new int[n];

		for(int index = 0; index < n; index++) {
			bars[index] = Integer.parseInt(lines[2 + index]);
		}
		test02(l, bars, assume);
	}

	private static void test02(int l, int[] bars, int assume) throws Exception {
		System.out.println("assume: " + assume);
		int ans = perform(l, bars);
		System.out.println("ans: " + ans);

		if(assume != ans) {
			throw new Exception("ng");
		}
	}

	private static void test03_a() throws Exception {
		_testTimeTotal = 0;
		_mainTimeTotal = 0;

		for(int l = 0; l <= 10; l++) {
			test03_b(l);
		}
		for(int l = 10; l <= 100; l += 10) {
			test03_b(l);
		}
		for(int l = 100; l <= 1000; l += 100) {
			test03_b(l);
		}
		for(int c = 0; c < 100; c++) {
			test03_b(MathTools.random(5000));
		}

		System.out.println("_testTimeTotal: " + _testTimeTotal);
		System.out.println("_mainTimeTotal: " + _mainTimeTotal);
	}

	private static void test03_b(int l) throws Exception {
		test03_c(l, 0.0);
		test03_c(l, 0.25);
		test03_c(l, 0.5);
	}

	private static void test03_c(int l, double rate) throws Exception {
		test03(l, rate, 1, 100);
		test03(l, rate, 1, 1000);
		test03(l, rate, 1000, 2000);
		test03(l, rate, 1900, 2000);
		test03(l, rate, 1, 2000);
	}

	private static long _testTimeTotal;
	private static long _mainTimeTotal;

	private static void test03(int l, double rate, int barmin, int barmax) throws Exception {
		List<Integer> barList = new ArrayList<Integer>();

		for(int bar = barmin; bar <= barmax; bar++) {
			if(Math.random() < rate) {
				barList.add(Integer.valueOf(bar));
			}
		}
		ArrayTools.shuffle(barList);

		int[] bars = IntTools.toInts(barList);

		System.out.println("l: " + l);
		System.out.println("bars_length: " + bars.length);

		long testSt = System.currentTimeMillis();
		int assume = performTest(l, bars);
		long testEd = System.currentTimeMillis();
		int ans = perform(l, bars);
		long ed = System.currentTimeMillis();

		long testTime = testEd - testSt;
		long mainTime = ed - testEd;
		_testTimeTotal += testTime;
		_mainTimeTotal += mainTime;
		System.out.println("TEST_MILLIS: " + testTime);
		System.out.println("MAIN_MILLIS: " + mainTime);

		System.out.println("assume: " + assume);
		System.out.println("ans: " + ans);

		if(assume != ans) {
			throw new Exception("ng");
		}
	}

	private static int performTest(int l, int[] bars) {
		int ans = 0;

		for(int i = 0; i < bars.length; i++) {
			for(int j = i + 1; j < bars.length; j++) {
				for(int k = j + 1; k < bars.length; k++) {
					if(bars[i] + bars[j] + bars[k] == l) {
						ans++;
					}
				}
			}
		}
		return ans;
	}

	private static void test04(int l, double rate, int barmin, int barmax) throws Exception {
		List<Integer> barList = new ArrayList<Integer>();

		for(int bar = barmin; bar <= barmax; bar++) {
			if(Math.random() < rate) {
				barList.add(Integer.valueOf(bar));
			}
		}
		ArrayTools.shuffle(barList);

		int[] bars = IntTools.toInts(barList);

		System.out.println("l: " + l);
		System.out.println("bars_length: " + bars.length);

		long st = System.currentTimeMillis();
		int ans = perform(l, bars);
		long ed = System.currentTimeMillis();

		System.out.println("MILLIS: " + (ed - st));
		System.out.println("ans: " + ans);
	}

	// ####
	// ####
	// ####

	private static int perform(int l, int[] bars) {
		bars = IntTools.getSorted(bars, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return a.intValue() - b.intValue();
			}
		});

		int ans = 0;

		for(int index = 0; index < bars.length - 2 && bars[index] * 3 + 3 <= l; index++) {
			ans += searchTwoRange(l - bars[index], bars, index + 1, bars.length - 1);
		}
		return ans;
	}

	private static int searchTwoRange(int l, int[] bars, int i, int j) {
		int ans = 0;

		while(i < j) {
			int sum = bars[i] + bars[j];

			if(sum < l) {
				i++;
			}
			else if(l < sum) {
				j--;
			}
			else {
				ans++;
				i++;
				j--;
			}
		}
		return ans;
	}
}
