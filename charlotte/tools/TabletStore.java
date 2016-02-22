package charlotte.tools;

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
			_count += count; // XXX

			if(_oneTime4Add != null) {
				_oneTime4Add.add();
				_oneTime4Add = null;
			}
		}
	}

	public boolean get() {
		return get(1);
	}

	public boolean get(int count) {
		return get(count, Long.MAX_VALUE);
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
}
