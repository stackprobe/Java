package teatime.server;

import charlotte.htt.HttServer;
import charlotte.tools.ThreadTools;

public class TeaTime {
	private TeaTimeService _service;

	public TeaTime(TeaTimeService service) {
		_service = service;
	}

	private Thread _th = null;

	public void start() {
		end();
		_th = new Thread() {
			@Override
			public void run() {
				try {
					_service.dead = false;
					HttServer.perform(_service);
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};
		_th.start();
	}

	public void end() {
		_service.dead = true;
		ThreadTools.join(_th);
		_th = null;
	}
}
