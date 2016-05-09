package charlotte.satellite.options;

import charlotte.satellite.MutexObject;
import charlotte.satellite.WinAPITools;
import charlotte.tools.ByteBuffer;
import charlotte.tools.SecurityTools;

/**
 * 遅すぎwarotawww
 *
 */
public class Mutector {
	private static final String COMMON_ID = "{fab3c841-8891-4273-8bd1-50525845fea7}"; // shared_uuid

	private static final int M_SENDER = 0;
	private static final int M_RECVER = 1;
	private static final int M_SYNC_0 = 2;
	private static final int M_SYNC_1 = 3;
	private static final int M_SYNC_2 = 4;
	private static final int M_BIT_0_0 = 5;
	//private static final int M_BIT_0_1 = 6;
	//private static final int M_BIT_0_2 = 7;
	private static final int M_BIT_1_0 = 8;
	//private static final int M_BIT_1_1 = 9;
	//private static final int M_BIT_1_2 = 10;

	private static final int M_MAX = 11; // num of M_

	private MutexObject[] _mtxs = new MutexObject[M_MAX];
	private boolean[] _statuses = new boolean[M_MAX];

	public Mutector(String name) throws Exception {
		String ident = COMMON_ID + "_" + SecurityTools.getSHA512_128String(name);

		for(int index = 0; index < M_MAX; index++) {
			_mtxs[index] = new MutexObject(ident + "_" + index);
		}
	}

	public void close() {
		if(_mtxs != null) {
			setAll(false);

			_mtxs = null;
			_statuses = null;
		}
	}

	public void setAll(boolean status) {
		for(int index = 0; index < M_MAX; index++) {
			set(index, status);
		}
	}

	public void set(int index, boolean status) {
		if(_statuses[index] != status) {
			if(status) {
				_mtxs[index].waitOne(WinAPITools.INFINITE);
			}
			else {
				_mtxs[index].release();
			}
			_statuses[index] = status;
		}
	}

	public boolean trySet(int index) {
		if(_statuses[index]) {
			throw null;
		}
		if(_mtxs[index].waitOne(0)) {
			_statuses[index] = true;
			return true;
		}
		return false;
	}

	public boolean get(int index) {
		if(_statuses[index]) {
			throw null;
		}
		if(_mtxs[index].waitOne(0)) {
			_mtxs[index].release();
			return false;
		}
		return true;
	}

	public static class Sender {
		private Mutector _m;

		public Sender(String name) throws Exception {
			_m = new Mutector(name);
		}

		/**
		 * Recver.perform() 実行中ではない -> 即例外
		 * 別の Sender 送信中 -> 終わるまで待つ。
		 * @param message
		 * @throws Exception
		 */
		public void send(byte[] message) throws Exception {
			if(message == null) {
				throw new IllegalArgumentException();
			}
			_m.set(M_SENDER, true);

			try {
				// Recver.perform() 実行中かどうか検査
				{
					if(_m.trySet(M_SYNC_0) &&
							_m.trySet(M_SYNC_1) &&
							_m.trySet(M_SYNC_2)
							) {
						throw new Exception("recver is not running");
					}
					_m.set(M_SYNC_0, false);
					_m.set(M_SYNC_1, false);
					_m.set(M_SYNC_2, false);
				}

				sendBit(true, true);

				for(int index = 0; index < message.length; index++) {
					for(int bit = 1 << 7; bit != 0; bit >>= 1) {
						if((message[index] & bit) != 0) {
							sendBit(false, true);
						}
						else {
							sendBit(true, false);
						}
					}
				}
				sendBit(false, false);
				sendBit(false, false);
				sendBit(false, false);
			}
			finally {
				_m.setAll(false);
			}
		}

		private int _m0 = 0;
		private int _m1 = 1;

		private void sendBit(boolean b0, boolean b1) {
			_m0++;
			_m1++;
			_m0 %= 3;
			_m1 %= 3;

			_m.set(M_SYNC_0 + _m1, true);
			_m.set(M_SYNC_0 + _m0, false);
			_m.set(M_BIT_0_0 + _m1, b0);
			_m.set(M_BIT_1_0 + _m1, b1);
		}

		public void close() {
			if(_m != null) {
				_m.close();
				_m = null;
			}
		}
	}

	public static abstract class Recver {
		private Mutector _m;
		public int recvSizeMax = 20000000;

		public Recver(String name) throws Exception {
			_m = new Mutector(name);
		}

		/**
		 * 別の Recver 受信中 -> 即例外
		 * @throws Exception
		 */
		public void perform() throws Exception {
			if(_m.trySet(M_RECVER) == false) {
				throw new Exception("already perform() running");
			}
			try {
				for(; ; ) {
					recvBit(0, 1, 2);
					recvBit(1, 2, 0);
					recvBit(2, 0, 1);

					if(2000 <= _elapsed) {
						_elapsed -= 2000;

						if(interlude() == false) {
							break;
						}
					}
				}
			}
			finally {
				_m.setAll(false);
			}
		}

		private int _millis = 0;
		private int _elapsed = 0;

		private void recvBit(int m0, int m1, int m2) throws Exception {
			_m.set(M_SYNC_0 + m1, true);

			// idling
			{
				if(_m.get(M_SYNC_0 + m2) == false) {
					if(_millis < 2000) {
						_millis += 100;
					}
					Thread.sleep(_millis);
				}
				else {
					_millis = 0;
				}
				_elapsed += _millis + 1;
			}

			_m.set(M_SYNC_0 + m0, false);

			{
				boolean b0 = _m.get(M_BIT_0_0 + m1);
				boolean b1 = _m.get(M_BIT_1_0 + m1);

				if(b0 && b1) {
					_buff = new ByteBuffer();
					_buffSize = 0;
					_bChr = 0;
					_bIndex = 0;
				}
				else if(_buff == null) {
					// noop
				}
				else if(b0) {
					recvedBit(0);
				}
				else if(b1) {
					recvedBit(1);
				}
				else {
					recved(_buff.getBytes());

					_buff = null;
					_buffSize = -1;
					_bChr = -1;
					_bIndex = -1;
				}
			}
		}

		private ByteBuffer _buff = null;
		private int _buffSize = 0;
		private int _bChr = -1;
		private int _bIndex = -1;

		private void recvedBit(int bit) throws Exception {
			_bChr <<= 1;
			_bChr |= bit;
			_bIndex++;

			if(_bIndex == 8) {
				if(recvSizeMax <= _buffSize) {
					throw new Exception("受信サイズ超過");
				}
				_buff.add((byte)_bChr);
				_buffSize++;
				_bChr = 0;
				_bIndex = 0;
			}
		}

		public void close() {
			if(_m != null) {
				_m.close();
				_m = null;
			}
		}

		public abstract boolean interlude(); // ret: ? 継続する。
		public abstract void recved(byte[] message);
	}
}
