package charlotte_test.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.DebugTools;
import charlotte.tools.MathTools;
import charlotte.tools.OrderedSet;
import charlotte.tools.StringTools;

public class OrderedSetTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		OrderedSet<String> set = new OrderedSet<String>(StringTools.comp);
		DummyStringOrderedSet set_d = new DummyStringOrderedSet();

		if(set.size() != 0) {
			throw null;
		}
		if(set.getList().size() != 0) {
			throw null;
		}

		for(int c = 0; c < 1000000; c++) {
			String element = DebugTools.makeRandString(StringTools.DIGIT, 3);

			switch(MathTools.random(3)) {
			case 0:
				set.add(element);
				set_d.add(element);
				break;

			case 1:
				set.shift(element);
				set_d.shift(element);
				break;

			case 2:
				set.remove(element);
				set_d.remove(element);
				break;

			default:
				throw null;
			}

			System.out.println(c + ", " + set_d.size());

			if(set.size() != set_d.size()) {
				throw null;
			}
			if(ArrayTools.<String>isSame(set.getList(), set_d.getList(), StringTools.comp) == false) {
				throw null;
			}
		}
	}

	private static class DummyStringOrderedSet {
		private List<String> _list = new ArrayList<String>();

		public void add(String element) {
			if(ArrayTools.contains(_list, element, StringTools.comp) == false) {
				_list.add(element);
			}
		}

		public void shift(String element) {
			if(ArrayTools.contains(_list, element, StringTools.comp) == false) {
				_list.add(0, element);
			}
		}

		public void remove(String element) {
			int index = ArrayTools.indexOf(_list, element, StringTools.comp);

			if(index != -1) {
				_list.remove(index);
			}
		}

		public int size() {
			return _list.size();
		}

		public List<String> getList() {
			return _list;
		}
	}
}
