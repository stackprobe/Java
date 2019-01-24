package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedList<T> {
	private List<T> _list = new ArrayList<T>();
	private boolean _sorted = true;
	private Comparator<T> _comp;

	public SortedList(Comparator<T> comp) {
		_comp = comp;
	}

	public void add(T element) {
		_list.add(element);
		_sorted = false;
	}

	public int size() {
		return _list.size();
	}

	public T get(int index) {
		sortIfNeed();
		return _list.get(index);
	}

	public void remove(int index) {
		sortIfNeed();
		_list.remove(index);
	}

	private void sortIfNeed() {
		if (_sorted == false) {
			sort();
			_sorted = true;
		}
	}

	private void sort() {
		ArrayTools.<T>sort(_list, _comp);
	}

	public boolean contains(T target) {
		return indexOf(target) != -1;
	}

	public int indexOf(T target) {
		sortIfNeed();

		int l = -1;
		int r = _list.size();

		while (l + 1 < r) {
			int m = (l + r) / 2;
			int ret = _comp.compare(_list.get(m), target);

			if (ret < 0) {
				l = m;
			}
			else if (0 < ret) {
				r = m;
			}
			else {
				return m;
			}
		}
		return -1; // not found
	}

	public int leftIndexOf(T target) {
		sortIfNeed();

		int l = 0;
		int r = _list.size();

		while (l < r) {
			int m = (l + r) / 2;
			int ret = _comp.compare(_list.get(m), target);

			if (ret < 0) {
				l = m + 1;
			}
			else {
				r = m;
			}
		}
		return l;
	}

	public int rightIndexOf(T target) {
		sortIfNeed();

		int l = -1;
		int r = _list.size() - 1;

		while (l < r) {
			int m = (l + r + 1) / 2;
			int ret = _comp.compare(_list.get(m), target);

			if (0 < ret) {
				r = m - 1;
			}
			else {
				l = m;
			}
		}
		return r;
	}

	public int[] getRange(T target) {
		return new int[] { leftIndexOf(target), rightIndexOf(target) };
	}

	public int[] getRangeWithEdge(T target) {
		return new int[] { leftIndexOf(target) - 1, rightIndexOf(target) + 1 };
	}

	public SubList<T> getMatch(T target) {
		final int[] range = getRange(target);

		return new SubList<T>() {
			@Override
			public int size() {
				return range[1] - range[0] - 1;
			}

			@Override
			public T get(int index) {
				return _list.get(range[0] + index);
			}
		};
	}

	public SubList<T> getMatchWithEdge(T target) {
		final int[] range = getRangeWithEdge(target);

		return new SubList<T>() {
			@Override
			public int size() {
				return range[1] - range[0] + 1;
			}

			@Override
			public T get(int index) {
				if(index < 0 || _list.size() <= index) {
					return null; // out of range
				}
				return _list.get(range[0] + index);
			}
		};
	}

	private Comparator<T> _lastComp;

	private void setComp(Comparator<T> comp) {
		_lastComp = _comp;
		_comp = comp;
	}

	private void restoreComp() {
		_comp = _lastComp;
	}

	public SubList<T> getMatch(T target, Comparator<T> comp) {
		setComp(comp);
		try {
			return getMatch(target);
		}
		finally {
			restoreComp();
		}
	}
}
