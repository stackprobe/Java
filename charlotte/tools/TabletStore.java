package charlotte.tools;

/**
 * thread safe
 *
 */
public class TabletStore {
	private final Object SYNCROOT = new Object();
	private final Object SYNCROOT_get = new Object();
	private int _count;
	private OneTime _oneTime4Add = null;

	public TabletStore() {
		this(0);
	}

	public TabletStore(int count) {
		if(count < 0) {
			throw new IllegalArgumentException();
		}
		_count = count;
	}

	public void add() {
		add(1);
	}

	public void add(int count) {
		if(count < 0) {
			throw new IllegalArgumentException();
		}
		synchronized(SYNCROOT) {
			_count += count;

			if(_oneTime4Add != null) {
				_oneTime4Add.add();
				_oneTime4Add = null;
			}
		}
	}

	public void mustGet() {
		mustGet(1);
	}

	public void mustGet(int count) {
		while(get(count) == false);
	}

	public boolean get() {
		return get(1);
	}

	public boolean get(int count) {
		return get(count, 2000);
		//return get(count, 30000);
		//return get(count, 60000);
		//return get(count, Long.MAX_VALUE);
	}

	public boolean get(int count, long millis) {
		if(count < 0) {
			throw new IllegalArgumentException();
		}
		if(millis < 0) {
			throw new IllegalArgumentException();
		}
		synchronized(SYNCROOT_get) {
			OneTime oneTime;

			synchronized(SYNCROOT) {
				if(count <= _count) {
					_count -= count;
					return true;
				}
				if(_ending) {
					millis = Math.min(millis, 100);
				}
				oneTime = new OneTime(millis);
				_oneTime4Add = oneTime;
			}
			oneTime.get();

			synchronized(SYNCROOT) {
				_oneTime4Add = null;

				if(count <= _count) {
					_count -= count;
					return true;
				}
			}
		}
		return false;
	}

	private static class OneTime {
		private Thread _th;

		public OneTime(final long millis) {
			_th = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(millis);
					}
					catch(InterruptedException e) {
						// noop
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};
			_th.start();
		}

		public void add() {
			ThreadTools.interrupt(_th);
		}

		public void get() {
			ThreadTools.join(_th);
		}
	}

	private boolean _ending = false;

	/**
	 * プロセス終了直前などに呼ぶと吉。
	 */
	public void ending() {
		synchronized(SYNCROOT) {
			_ending = true;
		}
		add(0);
	}
}
