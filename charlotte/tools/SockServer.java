package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.SwingUtilities;

public abstract class SockServer {
	private int _portNo;
	private boolean _invokeLaterFlag;

	public SockServer() {
		this(60000);
	}

	public SockServer(int portNo) {
		setPortNo(portNo);
	}

	public void setPortNo(int portNo) {
		_portNo = portNo;
	}

	public void setInvokeLaterFlag(boolean flag) {
		_invokeLaterFlag = flag;
	}

	private QueueData<Socket> _connections = new QueueData<Socket>();
	private int _connectionThCount;
	private final Object SYNCROOT = new Object();
	private ServerSocket _ss;

	public void perform() {
		try {
			bind();
			listen();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		unbind();
	}

	public void bind() throws Exception {
		_ss = new ServerSocket();
		_ss.setReuseAddress(true);
		_ss.setSoTimeout(2000);
		InetSocketAddress isa = new InetSocketAddress(_portNo);
		_ss.bind(isa);
	}

	public void listen() {
		try {
			for(; ; ) {
				try {
					if(interlude() == false) {
						break;
					}
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
				try {
					Socket sock = _ss.accept();

					_connections.add(sock);

					Thread connectionTh = new Thread() {
						@Override
						public void run() {
							if(_invokeLaterFlag) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										run2();
									}
								});
							}
							else {
								run2();
							}
						}

						private void run2() {
							Socket sock;

							synchronized(SYNCROOT) {
								sock = _connections.poll();

								if(sock == null) {
									return;
								}
								_connectionThCount++;
							}

							try {
								connectionTh(sock);
							}
							catch(Throwable e) {
								e.printStackTrace(System.out);
							}

							try {
								sock.close();
							}
							catch(Throwable e) {
								e.printStackTrace();
							}

							synchronized(SYNCROOT) {
								_connectionThCount--;
							}
						}
					};
					connectionTh.start();
				}
				catch(SocketTimeoutException e) {
					// ignore
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		waitForAllConnectionThEnd();
	}

	private void waitForAllConnectionThEnd() {
		try {
			synchronized(SYNCROOT) {
				while(1 <= _connections.size()) {
					Socket sock = _connections.poll();

					try {
						sock.close();
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			}
			int millis = 100;

			for(; ; ) {
				synchronized(SYNCROOT) {
					if(_connectionThCount == 0) {
						break;
					}
				}
				Thread.sleep(millis);

				if(millis < 2000) {
					millis += 100;
				}
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public void unbind() {
		try {
			_ss.close();
			_ss = null;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	protected abstract boolean interlude() throws Exception; // ret: true -> 継続, false -> 終了
	protected abstract void connectionTh(Socket sock) throws Exception;
}
