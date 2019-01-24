package charlotte_test.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.MathTools;
import charlotte.tools.Sorter;
import charlotte.tools.StringTools;

public class SorterTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		test01(1);
		test01(2);
		test01(3);
		test01(4);

		test01_b(5);
	}

	private static void test01(int algo) throws Exception {
		test01_cvt(algo, 1, 1, 1);
		test01_cvt(algo, 2, 2, 10);
		test01_cvt(algo, 3, 3, 10);
		test01_cvt(algo, 4, 4, 10);
		test01_cvt(algo, 5, 5, 10);

		test01_cvt(algo, 3,  3000, 100);
		test01_cvt(algo, 10, 1000, 100);
		test01_cvt(algo, 20,  500, 100);
		test01_cvt(algo, 30,  300, 100);
		test01_cvt(algo, 100, 100, 100);
		test01_cvt(algo, 300,  30, 100);
		test01_cvt(algo, 500,  20, 100);
		test01_cvt(algo, 1000, 10, 100);
//		test01_cvt(algo, 3000,  3, 100);

		/*
		 * クイックソートは同値が多いと遅くなる。<- 実装による。
		 */
		test01_cvt(algo, 3000,     1, 100);
		test01_cvt(algo, 3000,     2, 100);
		test01_cvt(algo, 3000,     3, 100);
		test01_cvt(algo, 3000,     4, 100);
		test01_cvt(algo, 3000,     5, 100);
		test01_cvt(algo, 3000,    10, 100);
		test01_cvt(algo, 3000,    30, 100);
		test01_cvt(algo, 3000,   100, 100);
		test01_cvt(algo, 3000,   300, 100);
		test01_cvt(algo, 3000,  1000, 100);
		test01_cvt(algo, 3000,  3000, 100);
		test01_cvt(algo, 3000, 10000, 100);
		test01_cvt(algo, 3000, 30000, 100);
	}

	private static void test01_b(int algo) throws Exception {
		test01_cvt(algo, 1, 1, 1);
		test01_cvt(algo, 2, 2, 10);
		test01_cvt(algo, 3, 3, 10);
		test01_cvt(algo, 4, 4, 10);
//		test01_cvt(algo, 5, 5, 10);

		test01_cvt(algo, 5, 1, 1);
		test01_cvt(algo, 5, 5, 100);
		test01_cvt(algo, 5, 10, 100);

		test01_cvt(algo, 10, 1, 1);
		test01_cvt(algo, 10, 10, 100);
		test01_cvt(algo, 10, 20, 100);
	}

	private static void test01_cvt(int algo, int countMod, int valueMod, int testMax) throws Exception {
		System.out.println(algo + "_" + countMod + "_" + valueMod + "_" + testMax);

		long startedTime = System.currentTimeMillis();

		for(int test = 0; test < testMax; test++) {
			List<String> src = new ArrayList<String>();
			int count = MathTools.random(countMod);

			for(int index = 0; index < count; index++) {
				src.add("" + MathTools.random(valueMod));
			}
			List<String> list1 = ArrayTools.copy(src);
			List<String> list2 = ArrayTools.copy(src);

			ArrayTools.sort(list1, StringTools.comp);
			Sorter.Sortable<String> list = Sorter.<String>create(list2, StringTools.comp);

			switch(algo) {
			case 1:
				Sorter.sort(list);
				break;
			case 2:
				Sorter.selectionSort(list);
				break;
			case 3:
				Sorter.combSort(list);
				break;
			case 4:
				Sorter.gnomeSort(list);
				break;
			case 5:
				Sorter.bogoSort(list);
				break;
			default:
				throw null;
			}

			if(list1.size() != list2.size()) {
				throw new Exception("SIZE_ERROR");
			}
			for(int index = 0; index < list1.size(); index++) {
				if(list1.get(index).equals(list2.get(index)) == false) {
					throw new Exception("ORDER_ERROR");
				}
			}
		}
		System.out.println("" + (System.currentTimeMillis() - startedTime));
	}
}
