package charlotte_test.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.SortedList;
import charlotte.tools.SystemTools;

public class SortedListTest {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		for(int c = 0; c < 10000; c++) {
			test01(0, 1000, SystemTools.random(2000));
		}
		System.out.println("OK!");
	}

	private static void test01(int minval, int maxval, int count) throws Exception {
		SortedList<Integer> sl = new SortedList<Integer>(new IntComp());
		P_SortedList<Integer> psl = new P_SortedList<Integer>(new IntComp());

		for(int c = 0; c < count; c++) {
			int value = SystemTools.random(minval, maxval);

			sl.add(value);
			psl.list.add(value);
		}

		for(int c = 0; c < 20; c++) {
			int value = SystemTools.random(minval, maxval);

			int[] r1 = sl.getRange(value);
			int[] r2 = psl.getRange(value);

			System.out.println(r1[0] + ", " + r1[1] + ", " + (r1[1] - r1[0])); // test

			if(r1[0] != r2[0] || r1[1] != r2[1]) {
				throw new Exception("ng");
			}
		}
	}

	private static class P_SortedList<T> {
		public List<T> list = new ArrayList<T>();
		public Comparator<T> comp;

		public P_SortedList(Comparator<T> cmp) {
			comp = cmp;
		}

		public int[] getRange(T ferret) {
			ArrayTools.sort(list, comp);

			return new int[] {
					getL(ferret),
					getH(ferret)
			};
		}

		private int getL(T ferret) {
			int index;

			for(index = 0;
					index < list.size() &&
					comp.compare(list.get(index), ferret) < 0;
					index++
					) {
			}
			return index - 1;
		}

		private int getH(T ferret) {
			int index;

			for(index = list.size() - 1;
					0 <= index &&
					0 < comp.compare(list.get(index), ferret);
					index--
					) {
			}
			return index + 1;
		}
	}

	private static class IntComp implements Comparator<Integer> {
		@Override
		public int compare(Integer a, Integer b) {
			return a.intValue() - b.intValue();
		}
	}
}