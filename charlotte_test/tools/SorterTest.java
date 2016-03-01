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
		test01_cvt(1, 1, 1);
		test01_cvt(2, 2, 10);
		test01_cvt(5, 5, 10);

		test01_cvt(3,  3000, 100);
		test01_cvt(10, 1000, 100);
		test01_cvt(20,  500, 100);
		test01_cvt(30,  300, 100);
		test01_cvt(100, 100, 100);
		test01_cvt(300,  30, 100);
		test01_cvt(500,  20, 100);
		test01_cvt(1000, 10, 100);
		test01_cvt(3000,  3, 100);

		/*
		 * クイックソートは同値が多いと遅くなる。<- 実装による。
		 */
		test01_cvt(3000,  10, 100);
		test01_cvt(3000,  30, 100);
		test01_cvt(3000, 100, 100);
	}

	private static void test01_cvt(int countMod, int valueMod, int testMax) throws Exception {
		System.out.println(countMod + "_" + valueMod + "_" + testMax);

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

			final List<String> f_list = list2;

			Sorter.sort(new Sorter.List<String>() {
				@Override
				public int size() {
					return f_list.size();
				}

				@Override
				public String get(int index) {
					return f_list.get(index);
				}

				@Override
				public void swap(int i, int j) {
					ArrayTools.swap(f_list, i, j);
				}

				@Override
				public int compare(String a, String b) {
					return StringTools.comp.compare(a, b);
				}
			});

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
