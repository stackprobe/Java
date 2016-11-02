package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;

public abstract class Threader<T> implements ValueSetter<T>, Closeable {
	private final Object SYNCROOT = new Object();
	private Thread _th = null;
	private ValueStore<T> _entries;

	public Threader(ValueStore<T> entries) {
		_entries = entries;
	}

	public void add(T entry) {
		if(entry == null) {
			throw new IllegalArgumentException("entry is null");
		}
		try {
			synchronized(SYNCROOT) {
				_entries.set(entry);

				if(_th == null) {
					_th = new Thread() {
						@Override
						public void run() {
							for(; ; ) {
								T next;

								synchronized(SYNCROOT) {
									next = _entries.get();

									if(next == null) {
										_th = null;
										break;
									}
								}
								set(next);
							}
						}
					};
					_th.start();
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		synchronized(SYNCROOT) {
			if(_th != null) {
				ThreadTools.join(_th);
				_th = null;
			}
		}
	}
}
