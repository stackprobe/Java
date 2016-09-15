package charlotte.flowertact;

import charlotte.satellite.WinAPITools;
import charlotte.tools.QueueData;
import charlotte.tools.SecurityTools;
import charlotte.tools.ThreadTools;

public class RapidPOBox extends PostOfficeBox {
	private String _identHash;

	public RapidPOBox(String ident) throws Exception {
		super(ident);
		_identHash = SecurityTools.getSHA512_128String(ident);
	}

	private QueueData<byte[]> _sq = new QueueData<byte[]>();
	private QueueData<byte[]> _rq = new QueueData<byte[]>();
	private Object SYNCROOT = new Object();
	private boolean _sending;

	/**
	 * 非同期で送信するため、本メソッドが終了した時点で未送信・送信中である可能性がある。
	 * クラス解放時 _sq に残っているデータは破棄される。
	 */
	@Override
	public void send(byte[] sendData) throws Exception {
		synchronized(SYNCROOT) {
			_sq.add(sendData);

			if(_sending == false) {
				_sending = true;

				new Thread() {
					@Override
					public void run() {
						try {
							for(; ; ) {
								QueueData<byte[]> eSq;

								synchronized(SYNCROOT) {
									if(_sq.size() == 0) {
										_sending = false;
										break;
									}
									eSq =_sq;
									_sq = new QueueData<byte[]>();
								}
								WinAPITools.i().sendToFortewave(_identHash, eSq);
							}
						}
						catch(Throwable e) {
							e.printStackTrace();
						}
					}
				}
				.start();
			}
		}
	}

	/**
	 * クラス解放時 _rq に残っているデータは破棄される。
	 */
	@Override
	public byte[] recv(long millis) throws Exception {
		if(_rq.size() == 0) {
			WinAPITools.i().recvFromFortewave(_identHash, _rq, millis, 100, 5000000); // 5 MB
		}
		return _rq.poll();
	}

	@Override
	public void close() {
		super.close();

		// wait for send()_thread
		{
			long millis = 0L;
			long elapse = 0L;

			while(_sending) {
				if(millis == 0L) {
					System.out.println("[RPOB]送信スレッドの停止を待っています...");
				}
				if(millis < 2000L) {
					millis++;
				}
				ThreadTools.sleep(millis);
				elapse += millis;

				if(30000L < elapse) {
					System.err.println("[RPOB]送信スレッドが停止しません。elapse=" + elapse);
				}
			}
			System.out.println("[RPOB]送信スレッドは停止しています。");
		}
	}
}
