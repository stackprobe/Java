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

	private List<Thread> _connectionThCollection = new ArrayList<Thread>();
	private final Object SYNCROOT = new Object();

	public void listen() {
		ServerSocket ss = null;

		try {
			ss = new ServerSocket();
			ss.setReuseAddress(true);
			ss.setSoTimeout(2000);
			InetSocketAddress isa = new InetSocketAddress(_portNo);
			ss.bind(isa);

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
					final Socket sock = ss.accept();

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
								_connectionThCollection.remove(this);
							}
						}
					};
					connectionTh.start();

					synchronized(SYNCROOT) {
						_connectionThCollection.add(connectionTh);
					}
				}
				catch(SocketTimeoutException e) {
					// ignore
				}
			}
			waitForAllConnectionThEnd();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		finally {
			try {
				ss.close();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private void waitForAllConnectionThEnd() throws Exception {
		int millis = 100;

		for(; ; ) {
			synchronized(SYNCROOT) {
				if(_connectionThCollection.size() == 0) {
					break;
				}
			}
			Thread.sleep(millis);

			if(millis < 2000) {
				millis += 100;
			}
		}
	}

	protected abstract boolean interlude() throws Exception; // ret: true -> 継続, false -> 終了
	protected abstract void connectionTh(Socket sock) throws Exception;
}
