package charlotte.tools;

public abstract class RunnableEx {
	public abstract void run() throws Exception;

	private Throwable _e = null;

	public Runnable getRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					RunnableEx.this.run();
				}
				catch(Throwable e) {
					_e = e;
				}
			}
		};
	}

	public void relay() throws Exception {
		if(_e != null) {
			Throwable e = _e;

			_e = null;

			if(e instanceof Error) {
				throw (Error)e;
			}
			if(e instanceof Exception) {
				throw (Exception)e;
			}
			throw new Exception(e);
		}
	}

	public Runnable getUnrelayRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					RunnableEx.this.run();
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};
	}

	public static RuntimeException re(Throwable e) {
		if(e instanceof Error) {
			throw (Error)e;
		}
		if(e instanceof RuntimeException) {
			return (RuntimeException)e;
		}
		return new RuntimeException(e);
	}
}
