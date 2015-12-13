package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedList<T> {
	private List<T> _list;
	private Comparator<T> _comp;
	private boolean _sortedFlag;

	public SortedList(List<T> bind_list, Comparator<T> cmp, boolean sortedFlag) {
		_list = bind_list;
		_comp = cmp;
		_sortedFlag = sortedFlag;
	}

	public SortedList(Comparator<T> cmp) {
		this(new ArrayList<T>(), cmp, true);
	}

	public void add(T element) {
		_list.add(element);
		_sortedFlag = false;
	}

	public int size() {
		return _list.size();
	}

	public T get(int index) {
		trySort();
		return _list.get(index);
	}

	public void remove(int index) {
		trySort();
		_list.remove(index);
	}

	private void trySort() {
		if(_sortedFlag == false) {
			sort();
			_sortedFlag = true;
		}
	}

	private void sort() {
		ArrayTools.sort(_list, _comp);
	}

	public List<T> getMatch(T ferret) {
		List<T> buff = new ArrayList<T>();
		int[] lr = getRange(ferret);

		for(int index = lr[0] + 1; index < lr[1]; index++) {
			buff.add(get(index));
		}
		return buff;
	}

	public List<T> getEdgeMatch(T ferret) {
		List<T> buff = new ArrayList<T>();
		int[] lr = getRange(ferret);

		for(int index = lr[0]; index <= lr[1]; index++) {
			if(index == -1 || index == size()) {
				buff.add(null);
			}
			else {
				buff.add(get(index));
			}
		}
		return buff;
	}

	public int[] getRange(T ferret) {
		int l = -1;
		int r = size();

		while(l + 1 < r) {
			int m = (l + r) / 2;
			int ret = _comp.compare(get(m), ferret);

			if(ret == 0) {
				final Comparator<T> f_cmp = _comp;

				l = getBorder(l, m, ferret, new Comparator<T>() {
					@Override
					public int compare(T a, T b) {
						return f_cmp.compare(a, b) == 0 ? 1 : 0;
					}
				})[0];
				r = getBorder(m, r, ferret, _comp)[1];
				break;
			}
			if(ret < 0) {
				l = m;
			}
			else {
				r = m;
			}
		}
		return new int[] { l, r };
	}

	private int[] getBorder(int l, int r, T ferret, Comparator<T> cmp) {
		while(l + 1 < r) {
			int m = (l + r) / 2;
			int ret = cmp.compare(get(m), ferret);

			if(ret == 0) {
				l = m;
			}
			else {
				r = m;
			}
		}
		return new int[] { l, r };
	}
}
