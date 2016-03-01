package charlotte.tools;

import java.util.Comparator;
import java.util.Stack;

public abstract class Sorter {
	public static <T> void sort(List<T> list) {
		Stack<List<T>> parts = new Stack<List<T>>();

		parts.add(list);

		while(1 <= parts.size()) {
			List<T> part = parts.pop();

			if(part.size() < 16) {
				insertSort(part);
				continue;
			}
			int l = 0;
			int pivot = part.size() / 2;
			int r = part.size() - 1;

			for(; ; ) {
				//*
				while(compare(part, l, pivot) < 0) {
					l++;
				}
				while(compare(part, pivot, r) < 0) {
					r--;
				}
				/*/
				// 同値が多いと遅くなる!!!
				while(l < pivot && compare(part, l, pivot) <= 0) {
					l++;
				}
				while(pivot < r && compare(part, pivot, r) <= 0) {
					r--;
				}
				//*/
				if(l == r) {
					break;
				}
				part.swap(l, r);

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
			parts.push(new Part<T>(part, 0, pivot));
			parts.push(new Part<T>(part, pivot + 1, part.size() - pivot - 1));

			if(512 <= parts.size()) {
				combSort(list);
				break;
			}
		}
	}

	public static <T> void insertSort(List<T> list) {
		for(int l = 0; l + 1 < list.size(); l++) {
			int minPos = l;

			for(int r = l + 1; r < list.size(); r++) {
				if(compare(list, r, minPos) < 0) {
					minPos = r;
				}
			}
			if(minPos != l) {
				list.swap(l, minPos);
			}
		}
	}

	public static <T> void combSort(List<T> list) {
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

	public static <T> void gnomeSort(List<T> list) {
		for(int r = 1; r < list.size(); r++) {
			for(int l = r; 0 < l; l--) {
				if(compare(list, l - 1, l) <= 0) {
					break;
				}
				list.swap(l - 1, l);
			}
		}
	}

	public static class Part<T> extends List<T> {
		private List<T> _list;
		private int _begin;
		private int _size;

		public Part(List<T> list) {
			this(list, 0, list.size());
		}

		public Part(List<T> list, int begin, int size) {
			_list = list;
			_begin = begin;
			_size = size;
		}

		@Override
		public int size() {
			return _size;
		}

		@Override
		public T get(int index) {
			return _list.get(_begin + index);
		}

		@Override
		public void swap(int i, int j) {
			_list.swap(_begin + i, _begin + j);
		}

		@Override
		public int compare(T a, T b) {
			return _list.compare(a, b);
		}
	}

	public static abstract class List<T> implements Comparator<T> {
		public abstract int size();
		public abstract T get(int index);
		public abstract void swap(int i, int j);
	}

	private static <T> int compare(List<T> list, int i, int j) {
		return list.compare(list.get(i), list.get(j));
	}
}
