package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedList<T> {
	private List<T> _list;
	private Comparator<T> _comp;
	private boolean _sortedFlag;

	public static SortedList<String> create() {
		return new SortedList<String>(StringTools.comp);
	}

	public static SortedList<String> createIgnoreCase() {
		return new SortedList<String>(StringTools.compIgnoreCase);
	}

	public SortedList(Comparator<T> comp) {
		this(new ArrayList<T>(), comp, true);
	}

	public SortedList(List<T> bind_list, Comparator<T> comp, boolean sortedFlag) {
		_list = bind_list;
		_comp = comp;
		_sortedFlag = sortedFlag;
	}

	public void add(T element) {
		_list.add(element);
		_sortedFlag = false;
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
		if(_sortedFlag == false) {
			sort();
			_sortedFlag = true;
		}
	}

	private void sort() {
		ArrayTools.sort(_list, _comp);
	}

	/**
	 * マッチした範囲を返す。
	 * @param ferret
	 * @return
	 * _list が { A, B, C } のとき...
	 * B にマッチしたとき -> { B }
	 * A ～ B にマッチしたとき -> { A, B }
	 * B ～ C にマッチしたとき -> { B, C }
	 * A ～ C にマッチしたとき -> { A, B, C }
	 * A より前にマッチしたとき -> { }
	 * A ～ B の間にマッチしたとき -> { }
	 * B ～ C の間にマッチしたとき -> { }
	 * C より後にマッチしたとき -> { }
	 * _list が { } のとき -> { }
	 */
	public SubList<T> getMatch(T ferret) {
		return getMatch(ferret, _comp);
	}

	public SubList<T> getMatch(T ferret, Comparator<T> comp) {
		int[] lr = getRange(ferret, comp);
		return SubList.create(_list, lr[0] + 1, (lr[1] - lr[0]) - 1);
	}

	/**
	 * マッチした範囲と「両端の1つ先の要素」も含めて返す。
	 * @param ferret
	 * @return
	 * _list が { A, B, C } のとき...
	 * B にマッチしたとき -> { A, B, C }
	 * A ～ B にマッチしたとき -> { null, A, B, C }
	 * B ～ C にマッチしたとき -> { A, B, C, null }
	 * A ～ C にマッチしたとき -> { null, A, B, C, null }
	 * A より前にマッチしたとき -> { null, A }
	 * A ～ B の間にマッチしたとき -> { A, B }
	 * B ～ C の間にマッチしたとき -> { B, C }
	 * C より後にマッチしたとき -> { C, null }
	 * _list が { } のとき -> { null, null }
	 */
	public SubList<T> getMatchWithEdge(T ferret) {
		return getMatchWithEdge(ferret, _comp);
	}

	public SubList<T> getMatchWithEdge(T ferret, Comparator<T> comp) {
		final int[] lr = getRange(ferret, comp);

		if(lr[0] == -1 || lr[1] == size()) {
			return new SubList<T>() {
				@Override
				public int size() {
					return (lr[1] - lr[0]) + 1;
				}

				@Override
				public T get(int index) {
					index += lr[0];

					if(index == -1 || index == SortedList.this.size()) {
						return null;
					}
					return _list.get(index);
				}
			};
		}
		return SubList.create(_list, lr[0], (lr[1] - lr[0]) + 1);
	}

	/**
	 *
	 * @param ferret
	 * @return { ferret より小さい最後の位置。無ければ、-1, ferret より大きい最初の位置。無ければ、size() }
	 */
	public int[] getRange(T ferret) {
		return getRange(ferret, _comp);
	}

	/**
	 *
	 * @param ferret
	 * @param comp _comp と矛盾しないこと。comp.compare の引数は、右側が常に ferret になる。
	 * @return
	 */
	public int[] getRange(T ferret, final Comparator<T> comp) {
		int l = -1;
		int r = size();

		while(l + 1 < r) {
			int m = (l + r) / 2;
			int ret = comp.compare(get(m), ferret);

			if(ret == 0) {
				l = getBorder(l, m, ferret, new Comparator<T>() {
					@Override
					public int compare(T a, T b) {
						return comp.compare(a, b) == 0 ? 1 : 0;
					}
				})[0];
				r = getBorder(m, r, ferret, comp)[1];
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

	private int[] getBorder(int l, int r, T ferret, Comparator<T> comp) {
		while(l + 1 < r) {
			int m = (l + r) / 2;
			int ret = comp.compare(get(m), ferret);

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
