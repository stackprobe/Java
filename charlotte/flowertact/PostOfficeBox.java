package charlotte.flowertact;

import java.io.File;

import charlotte.satellite.MutexObject;
import charlotte.satellite.NamedEventObject;
import charlotte.satellite.SatellizerTools;
import charlotte.satellite.WinAPITools;
import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class PostOfficeBox {
	private final String IDENT_PREFIX = "Fortewave_{d8600f7d-1ff4-47f3-b1c9-4b5aa15b6461}_"; // shared_uuid@g
	private String _ident;
	private MutexObject _mutex;
	private NamedEventObject _messagePostEvent;
	private String _messageDir;

	public PostOfficeBox(String ident) throws Exception {
		_ident = IDENT_PREFIX + SecurityTools.getSHA512_128String(ident);
		_mutex = new MutexObject(_ident + "_m");
		_messagePostEvent = new NamedEventObject(_ident + "_e");
		_messageDir = FileTools.combine(SatellizerTools.getTmp(), _ident);
	}

	public void clear() {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			FileTools.remove(_messageDir);
		}
		finally {
			_mutex.release();
		}
	}

	public void send(byte[] sendData) throws Exception {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			getMessageRange();
			tryRenumber();

			if(_lastNo == -1) {
				FileTools.mkdir(_messageDir);
			}
			FileTools.writeAllBytes(
					FileTools.combine(_messageDir, StringTools.zPad(_lastNo + 1, 4)),
					sendData
					);
		}
		finally {
			_mutex.release();
		}
		_messagePostEvent.set();
	}

	public byte[] recv(long millis) throws Exception {
		byte[] recvData = tryRecv();

		if(recvData == null) {
			_messagePostEvent.waitOne(millis);
			recvData = tryRecv();
		}
		return recvData;
	}

	private byte[] tryRecv() throws Exception {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			getMessageRange();

			if(_firstNo != -1) {
				String file = FileTools.combine(_messageDir, StringTools.zPad(_firstNo, 4));
				byte[] recvData = FileTools.readAllBytes(file);

				FileTools.delete(file);

				if(_firstNo == _lastNo) {
					FileTools.delete(_messageDir);
				}
				return recvData;
			}
		}
		finally {
			_mutex.release();
		}
		return null;
	}

	private int _firstNo;
	private int _lastNo;

	private void getMessageRange() {
		String[] files = new File(_messageDir).list();

		if(files == null) {
			_firstNo = -1;
			_lastNo = -1;
		}
		else {
			_firstNo = Integer.MAX_VALUE;
			_lastNo = 0;

			for(String file : files) {
				int no = Integer.parseInt(file);

				_firstNo = Math.min(_firstNo, no);
				_lastNo = Math.max(_lastNo, no);
			}
		}
	}

	private void tryRenumber() {
		if(1000 < _firstNo) {
			renumber();
		}
	}

	private void renumber() {
		for(int no = _firstNo; no <= _lastNo; no++) {
			String rFile = FileTools.combine(_messageDir, StringTools.zPad(no, 4));
			String wFile = FileTools.combine(_messageDir, StringTools.zPad(no - _firstNo, 4));

			new File(rFile).renameTo(new File(wFile));
		}
		_lastNo -= _firstNo;
		_firstNo = 0;
	}

	public void pulse() {
		_messagePostEvent.set();
	}

	public void close() {
		_messagePostEvent.close();
	}
}
