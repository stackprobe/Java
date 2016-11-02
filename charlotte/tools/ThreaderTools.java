package charlotte.tools;

import java.util.Comparator;
import java.util.TreeSet;

public class ThreaderTools {
	public static <T> Threader<T> createQueue(ValueSetter<T> setter) {
		return new Threader<T>(new QueueData<T>().toValueStore()) {
			@Override
			public void set(T entry) {
				setter.set(entry);
			}
		};
	}

	public static Threader<Runnable> createRunnable() {
		return new Threader<Runnable>(new QueueData<Runnable>().toValueStore()) {
			@Override
			public void set(Runnable runner) {
				runner.run();
			}
		};
	}

	public static <T> Threader<T> createSet(ValueSetter<T> setter, Comparator<T> comp) {
		return new Threader<T>(SetTools.toValueStore(new TreeSet<T>(comp))) {
			@Override
			public void set(T entry) {
				setter.set(entry);
			}
		};
	}

	public static Threader<String> createStringSet(ValueSetter<String> setter) {
		return createSet(setter, StringTools.comp);
	}

	public static Threader<String> createStringSetIgnoreCase(ValueSetter<String> setter) {
		return createSet(setter, StringTools.compIgnoreCase);
	}
}
