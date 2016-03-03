package charlotte.tools;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public abstract class Sorter {
	public static <T> void sort(Sortable<T> list) {
		Stack<Integer> ranges = new Stack<Integer>();

		ranges.add(0);
		ranges.add(list.size());

		while(1 <= ranges.size()) {
			int end = ranges.pop();
			int begin = ranges.pop();

			if(end - begin < 16) {
				insertSort(list, begin, end);
				continue;
			}
			if(100 <= ranges.size()) {
				combSort(list);
				break;
			}
			int l = begin;
			int pivot = (begin + end) / 2;
			int r = end - 1;

			for(; ; ) {
				//*
				while(compare(list, l, pivot) < 0) {
					l++;
				}
				while(compare(list, pivot, r) < 0) {
					r--;
				}
				/*/
				// 同値が多いと遅くなる!!!
				while(l < pivot && compare(list, l, pivot) <= 0) {
					l++;
				}
				while(pivot < r && compare(list, pivot, r) <= 0) {
					r--;
				}
				//*/
				if(l == r) {
					break;
				}
				list.swap(l, r);

				if(l == pivot) {
					pivot = r;
					l++;
				}
				else if(r == pivot) {
					pivot = l;
					r--;
				}
				else {
					l++;
					r--;
				}
			}
			ranges.add(begin);
			ranges.add(pivot);
			ranges.add(pivot + 1);
			ranges.add(end);
		}
	}

	public static <T> void insertSort(Sortable<T> list) {
		insertSort(list, 0, list.size());
	}

	public static <T> void insertSort(Sortable<T> list, int begin, int end) {
		for(int l = begin; l + 1 < end; l++) {
			int minPos = l;

			for(int r = l + 1; r < end; r++) {
				if(compare(list, r, minPos) < 0) {
					minPos = r;
				}
			}
			if(minPos != l) {
				list.swap(l, minPos);
			}
		}
	}

	public static <T> void combSort(Sortable<T> list) {
		int span = list.size();

		for(; ; ) {
			span *= 10;
			span /= 13;

			if(span < 2) {
				break;
			}
			if(span == 9 || span == 10) {
				span = 11;
			}
			for(int l = 0, r = span; r < list.size(); l++ ,r++) {
				if(0 < compare(list, l, r)) {
					list.swap(l, r);
				}
			}
		}
		gnomeSort(list);
	}

	public static <T> void gnomeSort(Sortable<T> list) {
		for(int r = 1; r < list.size(); r++) {
			for(int l = r; 0 < l; l--) {
				if(compare(list, l - 1, l) <= 0) {
					break;
				}
				list.swap(l - 1, l);
			}
		}
	}

	public static <T> void bogoSort(Sortable<T> list) {
		do {
			shuffle(list);
		}
		while(isSorted(list) == false);
	}

	public static abstract class Sortable<T> implements Comparator<T> {
		public abstract int size();
		public abstract T get(int index);
		public abstract void swap(int i, int j);

		public static <T> Sortable<T> create(final List<T> list, final Comparator<T> comp) {
			return new Sortable<T>() {
				@Override
				public int compare(T a, T b) {
					return comp.compare(a, b);
				}

				@Override
				public int size() {
					return list.size();
				}

				@Override
				public T get(int index) {
					return list.get(index);
				}

				@Override
				public void swap(int i, int j) {
					ArrayTools.swap(list, i, j);
				}
			};
		}

		public static <T> Sortable<T> create(final T[] arr, final Comparator<T> comp) {
			return new Sortable<T>() {
				@Override
				public int compare(T a, T b) {
					return comp.compare(a, b);
				}

				@Override
				public int size() {
					return arr.length;
				}

				@Override
				public T get(int index) {
					return arr[index];
				}

				@Override
				public void swap(int i, int j) {
					ArrayTools.swap(arr, i, j);
				}
			};
		}
	}

	public static <T> int compare(Sortable<T> list, int i, int j) {
		return list.compare(list.get(i), list.get(j));
	}

	public static <T> void shuffle(Sortable<T> list) {
		for(int i = 0; i < list.size() - 1; i++) {
			int j = MathTools.random(i, list.size() - 1);

			list.swap(i, j);
		}
	}

	public static <T> boolean isSorted(Sortable<T> list) {
		for(int i = 0; i < list.size() - 1; i++) {
			if(0 < compare(list, i, i + 1)) {
				return false;
			}
		}
		return true;
	}
}
