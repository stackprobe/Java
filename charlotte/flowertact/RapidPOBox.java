package charlotte.flowertact;

import charlotte.satellite.WinAPITools;
import charlotte.tools.QueueData;
import charlotte.tools.SecurityTools;

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
								WinAPITools.sendToFortewave(_identHash, eSq);
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

	@Override
	public byte[] recv(long millis) throws Exception {
		if(_rq.size() == 0) {
			WinAPITools.recvFromFortewave(_identHash, _rq, millis, 100);
		}
		return _rq.poll();
	}
}
