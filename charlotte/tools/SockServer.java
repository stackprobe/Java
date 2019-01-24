package charlotte.tools;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

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

	private final Object SYNCROOT = new Object();
	private QueueData<Socket> _connections = new QueueData<Socket>();
	private List<Thread> _connectionThs = new ArrayList<Thread>();
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
		_ss.setSoTimeout(2000); // _ss.accept()のタイムアウト
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

					synchronized(SYNCROOT) {
						_connections.add(sock);
					}

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
									new Exception("sock == null").printStackTrace();
									return;
								}
							}

							try {
								connectionTh(sock);
							}
							catch(Throwable e) {
								e.printStackTrace();
							}

							try {
								sock.close();
							}
							catch(Throwable e) {
								e.printStackTrace();
							}
						}
					};
					connectionTh.start();
					collectDeadConnectionTh();
					_connectionThs.add(connectionTh);
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

	private void collectDeadConnectionTh() {
		for(int index = 0; index < _connectionThs.size(); index++) {
			if(_connectionThs.get(index).isAlive() == false) {
				ArrayTools.fastRemove(_connectionThs, index);
				index--;
			}
		}
	}

	private void waitForAllConnectionThEnd() {
		try {
			long millis = 0L;

			for(; ; ) {
				collectDeadConnectionTh();

				if(_connectionThs.size() == 0) {
					break;
				}
				if(millis < 2000L) {
					millis++;
				}
				Thread.sleep(millis);
			}
			if(_connections.size() != 0) {
				throw new RuntimeException("_connections is not empty");
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
