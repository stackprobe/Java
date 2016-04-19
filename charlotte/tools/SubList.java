package charlotte.tools;

import java.util.List;

public abstract class SubList<T> {
	public abstract int size();
	public abstract T get(int index);

	public static <E> SubList<E> create(final E[] arr) {
		return new SubList<E>() {
			@Override
			public int size() {
				return arr.length;
			}

			@Override
			public E get(int index) {
				return arr[index];
			}
		};
	}

	public static <E> SubList<E> create(final List<E> list) {
		return new SubList<E>() {
			@Override
			public int size() {
				return list.size();
			}

			@Override
			public E get(int index) {
				return list.get(index);
			}
		};
	}

	public static <E> SubList<E> create(List<E> list, int startPos) {
		return create(list, startPos, list.size() - startPos);
	}

	public static <E> SubList<E> create(final List<E> list, final int startPos, final int size) {
		return new SubList<E>() {
			@Override
			public int size() {
				return size;
			}

			@Override
			public E get(int index) {
				return list.get(startPos + index);
			}
		};
	}
}
