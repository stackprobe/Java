package charlotte.xxxtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IList<T> implements Iterable<T> {
	private List<Iterable<T>> _list = new ArrayList<Iterable<T>>();

	public IList() {
	}

	public IList(Iterable<T> l) {
		add(l);
	}

	public IList(T... a) {
		add(a);
	}

	public IList<T> add(Iterable<T> l) {
		_list.add(l);
		return this;
	}

	public IList<T> add(T... a) {
		_list.add(Arrays.asList(a));
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>(){
			private Iterator<Iterable<T>> _ii = _list.iterator();
			private Iterator<T> _i;

			@Override
			public boolean hasNext() {
				if(_i == null || _i.hasNext() == false) {
					if(_ii.hasNext() == false)
						return false;

					_i = _ii.next().iterator();
				}
				return true;
			}

			@Override
			public T next() {
				if(this.hasNext() == false) {
					throw new NoSuchElementException();
				}
				return _i.next();
			}

			@Override
			public void remove() {
				// noop
			}
		};
	}
}
