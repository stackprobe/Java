package charlotte.satellite;

public class Satellizer {
	private static final String COMMON_ID = "{cb88beb3-4661-4399-98a8-a5dff3654347}"; // shared_uuid
	private Connection _conn;
	private boolean _connected;

	public Satellizer(String group, String ident) throws Exception {
		if(group == null) {
			throw new NullPointerException("group");
		}
		if(ident == null) {
			throw new NullPointerException("ident");
		}
		init();
		_conn = new Connection(group, ident);
	}

	private static boolean _inited;

	private static synchronized void init() throws Exception {
		if(_inited) {
			return;
		}
		_inited = true;
	}

	public static void listen(String group, String ident, long millis, final Server server) throws Exception {
		if(group == null) {
			throw new NullPointerException("group");
		}
		if(ident == null) {
			throw new NullPointerException("ident");
		}
		if(millis < 0) {
			throw new Exception("millis lt 0");
		}
		if(WinAPITools.INFINITE < millis) {
			throw new Exception("millis gt max");
		}
		if(server == null) {
			throw new Exception("server is null");
		}
		init();

		Satellizer stllzr = null;
		MutexObject mutex = new MutexObject(COMMON_ID);
		mutex.waitOne(WinAPITools.INFINITE);

		try {
			while(server.interlude()) {
				if(stllzr == null) {
					stllzr = new Satellizer(group, ident);
					stllzr._conn._listenFlag = true;
				}
				stllzr._connected = stllzr._conn.connect(millis, mutex);

				if(stllzr._connected) {
					final Satellizer f_stllzr = stllzr;
					stllzr = null;

					new Thread() {
						@Override
						public void run() {
							try {
								server.serviceTh(f_stllzr);
							}
							catch(Throwable e) {
								e.printStackTrace();
							}
							f_stllzr.close();
						}
					}
					.start();
				}
			}
		}
		finally {
			if(mutex.isLocking()) {
				mutex.release();
			}
			if(stllzr != null) {
				stllzr.close();
			}
		}
	}

	public interface Server {
		public boolean interlude() throws Exception;
		public void serviceTh(Satellizer stllzr) throws Exception;
	}

	public synchronized boolean connect(long millis) throws Exception {
		if(millis < 0) {
			throw new Exception("millis lt 0");
		}
		if(WinAPITools.INFINITE < millis) {
			throw new Exception("millis gt max");
		}
		if(_conn == null) {
			throw new Exception("already closed");
		}
		if(_connected) {
			throw new Exception("already connected");
		}
		MutexObject mutex = new MutexObject(COMMON_ID);
		mutex.waitOne(WinAPITools.INFINITE);

		try {
			_connected = _conn.connect(millis, mutex);
		}
		finally {
			mutex.release();
		}
		return _connected;
	}

	public synchronized void send(Object sendObj) throws Exception {
		if(sendObj == null) {
			throw new NullPointerException("sendObj");
		}
		if(_conn == null) {
			throw new Exception("already closed");
		}
		if(_connected == false) {
			throw new Exception("not connected");
		}
		byte[] sendData = new Serializer(sendObj).getBytes();
		_conn.send(sendData);
	}

	public synchronized Object recv(long millis) throws Exception {
		if(millis < 0) {
			throw new Exception("millis lt 0");
		}
		if(WinAPITools.INFINITE < millis) {
			throw new Exception("millis gt max");
		}
		if(_conn == null) {
			throw new Exception("already closed");
		}
		if(_connected == false) {
			throw new Exception("not connected");
		}
		byte[] recvData = _conn.recv(millis);

		if(recvData == null) {
			return null;
		}
		Object recvObj = new Deserializer(recvData).next();
		return recvObj;
	}

	public synchronized boolean isOtherSideDisconnected() throws Exception {
		if(_conn == null) {
			throw new Exception("already closed");
		}
		if(_connected == false) {
			throw new Exception("not connected");
		}
		return _conn.isDisconnected();
	}

	public synchronized void disconnect() {
		try {
			if(_connected) {
				_conn.disconnect();
				_connected = false;
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		if(_conn != null) {
			disconnect();
			_conn.close();
			_conn = null;
		}
	}
}
