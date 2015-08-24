package charlotte.flowertact;

import java.util.Comparator;

import charlotte.satellite.MutexObject;
import charlotte.satellite.NamedEventObject;
import charlotte.satellite.SatellizerTools;
import charlotte.satellite.WinAPITools;
import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;

public class PostOfficeBox {
	private final String IDENT_PREFIX = "Fortewave_{d8600f7d-1ff4-47f3-b1c9-4b5aa15b6461}_"; // shared_uuid:2
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
			int lastNo = getMessageRange()[1];

			if(lastNo == -1) {
				FileTools.mkdir(_messageDir);
			}
			FileTools.writeAllBytes(
					FileTools.combine(_messageDir, StringTools.zPad(lastNo + 1, 4)),
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
			int[] messageRange = getMessageRange();
			int firstNo = messageRange[0];
			int lastNo = messageRange[1];

			if(firstNo != -1) {
				String file = FileTools.combine(_messageDir, StringTools.zPad(firstNo, 4));
				byte[] recvData = FileTools.readAllBytes(file);

				FileTools.delete(file);

				if(firstNo == lastNo) {
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

	private int[] getMessageRange() {
		String[] files = FileTools.list(_messageDir);

		if(files == null) {
			return new int[] { -1, -1 };
		}
		ArrayTools.sort(files, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				return Integer.parseInt(a) - Integer.parseInt(b);
			}
		});
		return new int[] {
				Integer.parseInt(files[0]),
				Integer.parseInt(files[files.length - 1]),
		};
	}
}
