package charlotte.satellite;

import java.util.Arrays;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.QueueData;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.SystemTools;

public class Connection {
	private final static String COMMON_ID = "{f5f0b66c-3aa9-48ef-a924-9918f1c69b7a}"; // shared_uuid:3
	private String _group;
	private String _ident;
	private String _session;
	private String _commonDir;
	private String _groupDir;
	private String _identDir;
	private String _sessionDir;
	private MutexObject _mutex;
	private NamedEventObject _event;
	private DeadAndRemove _dar;
	private String _otherIdent;
	private String _otherSession;
	private String _otherIdentDir;
	private String _otherSessionDir;
	private NamedEventObject _otherEvent;
	private String _firstTimeFile;
	private MutexObject _procAliveMutex;
	private MutexObject _otherProcAliveMutex;

	public Connection(String group, String ident) throws Exception {
		_group = SecurityTools.getSHA512_128String(group);
		_ident = SecurityTools.getSHA512_128String(ident);
		_session = StringTools.getUUID();
		_commonDir = FileTools.combine(SatellizerTools.getTmp(), COMMON_ID);
		_groupDir = FileTools.combine(_commonDir, _group);
		_identDir = FileTools.combine(_groupDir, _ident);
		_sessionDir = FileTools.combine(_identDir, _session);
		_mutex = new MutexObject(COMMON_ID);
		_event = new NamedEventObject(_session);
		_firstTimeFile = _commonDir + "_1";
		_procAliveMutex = new MutexObject(_session + "_PA");
		_procAliveMutex.waitOne(WinAPITools.INFINITE);
	}

	public boolean _listenFlag;

	public boolean connect(long millis, MutexObject outerMutex) throws Exception {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			if(FileTools.exists(_firstTimeFile) == false) {
				FileTools.remove(_commonDir);
				FileTools.createFile(_firstTimeFile);
				WinAPITools.deleteDelayUntilReboot(_firstTimeFile);
			}
			FileTools.mkdir(_commonDir);
			FileTools.mkdir(_groupDir);
			FileTools.mkdir(_identDir);
			FileTools.mkdir(_sessionDir);
			_dar = new DeadAndRemove(COMMON_ID, _sessionDir);

			String pidFile = FileTools.combine(_sessionDir, "_PID");
			FileTools.writeAllBytes(pidFile, IntTools.toBytes(SystemTools.PID));

			if(_listenFlag) {
				String listenFile = FileTools.combine(_sessionDir, "_listen");
				FileTools.createFile(listenFile);
			}
			if(tryConnect()) {
				return true;
			}
			_mutex.release();
			outerMutex.release();
			_event.waitOne(millis);
			outerMutex.waitOne(WinAPITools.INFINITE);
			_mutex.waitOne(WinAPITools.INFINITE);

			if(tryConnect()) {
				return true;
			}
			FileTools.delete(pidFile);

			if(_listenFlag) {
				String listenFile = FileTools.combine(_sessionDir, "_listen");
				FileTools.delete(listenFile);
			}
			FileTools.tryDeleteDir(_sessionDir);
			FileTools.tryDeleteDir(_identDir);
			FileTools.tryDeleteDir(_groupDir);
			FileTools.tryDeleteDir(_commonDir);
		}
		finally {
			if(_mutex.isLocking()) {
				_mutex.release();
			}
		}
		_dar.dead();
		return false;
	}

	private boolean tryConnect() throws Exception {
		{
			String connectFile = FileTools.combine(_sessionDir, "_connect");

			if(FileTools.exists(connectFile)) {
				byte[] data = FileTools.readAllBytes(connectFile);
				String text = new String(data, StringTools.CHARSET_SJIS);
				List<String> tokens = StringTools.tokenize(text, "\n");

				int c = 0;
				_otherIdent = tokens.get(c++);
				_otherIdentDir = tokens.get(c++);
				_otherSession = tokens.get(c++);
				_otherSessionDir = tokens.get(c++);
				_otherEvent = new NamedEventObject(_otherSession);
				_otherProcAliveMutex = new MutexObject(_otherSession + "_PA");

				return true;
			}
		}

		_otherIdent = getOtherIdent();

		if(_otherIdent != null) {
			_otherIdentDir = FileTools.combine(_groupDir, _otherIdent);
			_otherSession = getOtherSession(_otherIdentDir);

			if(_otherSession != null) {
				_otherSessionDir = FileTools.combine(_otherIdentDir, _otherSession);

				{
					String[] tokens = new String[] {
							_ident,
							_identDir,
							_session,
							_sessionDir,
					};
					String text = StringTools.join("\n", Arrays.asList(tokens));
					byte[] data = text.getBytes(StringTools.CHARSET_SJIS);

					String connectFile = FileTools.combine(_otherSessionDir, "_connect");

					FileTools.writeAllBytes(connectFile, data);
				}

				{
					String connectFile = FileTools.combine(_sessionDir, "_connect");

					FileTools.createFile(connectFile);
				}

				_otherEvent = new NamedEventObject(_otherSession);
				_otherEvent.set();

				return true;
			}
		}
		return false;
	}

	private String getOtherIdent() {
		for(String otherIdent : FileTools.list(_groupDir)) {
			if(otherIdent.equalsIgnoreCase(_ident) == false) {
				return otherIdent;
			}
		}
		return null;
	}

	private String getOtherSession(String otherIdentDir) throws Exception {
		for(String otherSession : FileTools.list(otherIdentDir)) {
			String otherSessionDir = FileTools.combine(otherIdentDir, otherSession);
			String connectFile = FileTools.combine(otherSessionDir, "_connect");

			if(FileTools.exists(connectFile)) {
				continue;
			}
			_otherProcAliveMutex = new MutexObject(otherSession + "_PA");

			if(checkDisconnected(otherSessionDir)) {
				continue;
			}
			if(_listenFlag) {
				String listenFile = FileTools.combine(otherSessionDir, "_listen");

				if(FileTools.exists(listenFile)) {
					continue;
				}
			}
			return otherSession;
		}
		return null;
	}

	public void send(byte[] sendData) throws Exception {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			if(isDisconnected_nx()) {
				return;
			}
			for(int index = 0; ; index++) {
				String dataFile = FileTools.combine(_otherSessionDir, "" + index);

				if(FileTools.exists(dataFile) == false) {
					FileTools.writeAllBytes(dataFile, sendData);
					break;
				}
			}
		}
		finally {
			_mutex.release();
		}

		_otherEvent.set();
	}

	private QueueData<byte[]> _recvDataQueue = new QueueData<byte[]>();
	private int _recvDisconCount;

	public byte[] recv(long millis) throws Exception {
		if(_recvDataQueue.size() == 0) {
			_mutex.waitOne(WinAPITools.INFINITE);

			try {
				tryRecv();

				if(_recvDataQueue.size() == 0) {
					if(isDisconnected_nx()) {
						if(_recvDisconCount < 10) {
							_recvDisconCount++;
						}
						else {
							_mutex.release();
							Thread.sleep(Math.min(millis, 200));
						}
						return null;
					}
					_mutex.release();
					_event.waitOne(millis);
					_mutex.waitOne(WinAPITools.INFINITE);

					tryRecv();
				}
			}
			finally {
				if(_mutex.isLocking()) {
					_mutex.release();
				}
			}
		}
		return _recvDataQueue.poll();
	}

	private void tryRecv() throws Exception {
		for(int index = 0; ; index++) {
			String dataFile = FileTools.combine(_sessionDir, "" + index);

			if(FileTools.exists(dataFile) == false) {
				break;
			}
			_recvDataQueue.add(FileTools.readAllBytes(dataFile));
			FileTools.delete(dataFile);
		}
	}

	public boolean isDisconnected() throws Exception {
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			return isDisconnected_nx();
		}
		finally {
			_mutex.release();
		}
	}

	private boolean isDisconnected_nx() throws Exception {
		if(FileTools.exists(_otherSessionDir) == false) {
			return true;
		}
		return checkDisconnected(_otherSessionDir);
	}

	private boolean checkDisconnected(String otherSessionDir) throws Exception {
		if(cd_isDisconnected(otherSessionDir)) {
			FileTools.remove(otherSessionDir);
			return true;
		}
		return false;
	}

	private boolean cd_isDisconnected(String otherSessionDir) throws Exception {
		if(_otherProcAliveMutex.waitOne(0)) {
			_otherProcAliveMutex.release();
			return true;
		}

		// test
		/*
		{
			String pidFile = FileTools.combine(otherSessionDir, "_PID");

			if(FileTools.exists(pidFile) == false)
				return true;

			int pid = IntTools.read(FileTools.readAllBytes(pidFile));

			if(WinAPITools.isProcessAlive(pid) == false)
				return true;
		}
		*/

		return false;
	}

	public void disconnect() throws Exception {
		_dar.dead();
		_mutex.waitOne(WinAPITools.INFINITE);

		try {
			FileTools.tryDeleteDir(_identDir);
			FileTools.tryDeleteDir(_groupDir);
			FileTools.tryDeleteDir(_commonDir);
		}
		finally {
			_mutex.release();
		}

		_otherEvent.set();
		_otherEvent.close();
	}

	public void close() {
		_event.close();
		_procAliveMutex.release();
	}
}
