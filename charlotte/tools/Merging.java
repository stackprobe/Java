package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Merging {
	/**
	 *
	 * @param l
	 * @param r
	 * @param both
	 * @param both_r null ok
	 * @param comp
	 */
	public static <T> void merge(List<T> l, List<T> r, List<T> both, List<T> both_r, Comparator<T> comp) {
		List<T> l2 = new ArrayList<T>();
		List<T> r2 = new ArrayList<T>();

		l2.addAll(l);
		r2.addAll(r);

		l.clear();
		r.clear();

		ArrayTools.sort(l2, comp);
		ArrayTools.sort(r2, comp);
		// same
		/*
		Sorter.<T>sort(Sorter.<T>create(l2, comp));
		Sorter.<T>sort(Sorter.<T>create(r2, comp));
		*/

		Reader<T> l3 = new Reader<T>(l2);
		Reader<T> r3 = new Reader<T>(r2);

		ValueGetter<T> le = l3.next();
		ValueGetter<T> re = r3.next();

		while(le != null && re != null) {
			int ret = comp.compare(le.get(), re.get());

			if(ret < 0) {
				l.add(le.get());
				le = l3.next();
			}
			else if(0 < ret) {
				r.add(re.get());
				re = r3.next();
			}
			else {
				both.add(le.get());

				if(both_r != null) {
					both_r.add(re.get());
				}

				le = l3.next();
				re = r3.next();
			}
		}
		if(le != null) {
			l.add(le.get());
			l3.addFollowsTo(l);
		}
		if(re != null) {
			r.add(re.get());
			r3.addFollowsTo(r);
		}
	}

	private static class Reader<T> {
		private List<T> _src;

		public Reader(List<T> src) {
			_src = src;
		}

		private int _index = 0;

		public ValueGetter<T> next() {
			if(_index < _src.size()) {
				return new ValueBox<T>(_src.get(_index++));
			}
			return null;
		}

		public void addFollowsTo(List<T> dest) {
			for(; ; ) {
				ValueGetter<T> element = next();

				if(element == null) {
					break;
				}
				dest.add(element.get());
			}
		}
	}

	public static void mergeText(List<String> l, List<String> r, List<String> both, List<String> both_r) {
		merge(l, r, both, both_r, StringTools.comp);
	}

	public static void mergeTextIgnoreCase(List<String> l, List<String> r, List<String> both, List<String> both_r) {
		merge(l, r, both, both_r, StringTools.compIgnoreCase);
	}
}
