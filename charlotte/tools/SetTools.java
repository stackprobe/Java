package charlotte.tools;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SetTools {
	public static Set<String> create() {
		return new TreeSet<String>(StringTools.comp);
	}

	public static Set<String> createIgnoreCase() {
		return new TreeSet<String>(StringTools.compIgnoreCase);
	}

	public static <T> T poll(Set<T> set) {
		Iterator<T> i = set.iterator();

		if(i.hasNext() == false) {
			return null;
		}
		T next = i.next();
		i.remove();
		return next;
	}

	public static <T> ValueStore<T> toValueStore(Set<T> set) {
		return new ValueStore<T>() {
			@Override
			public T get() {
				return poll(set);
			}

			@Override
			public void set(T element) {
				set.add(element);
			}
		};
	}
}
