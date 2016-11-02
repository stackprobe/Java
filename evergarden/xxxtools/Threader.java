package evergarden.xxxtools;

import java.io.Closeable;
import java.io.IOException;

import charlotte.tools.QueueData;
import charlotte.tools.ThreadTools;

public class Threader implements Closeable {
	private final Object SYNCROOT = new Object();
	private Thread _th = null;
	private QueueData<Runnable> _runners = new QueueData<Runnable>();

	public void add(Runnable runner) {
		if(runner == null) {
			throw new IllegalArgumentException("runner is null");
		}
		synchronized(SYNCROOT) {
			_runners.add(runner);

			if(_th == null) {
				_th = new Thread() {
					@Override
					public void run() {
						for(; ; ) {
							Runnable next;

							synchronized(SYNCROOT) {
								next = _runners.poll();

								if(next == null) {
									_th = null;
									break;
								}
							}
							try {
								next.run();
							}
							catch(Throwable e) {
								e.printStackTrace();
							}
						}
					}
				};
				_th.start();
			}
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
