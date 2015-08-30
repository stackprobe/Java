package charlotte.flowertact;

import charlotte.satellite.Deserializer;
import charlotte.satellite.Serializer;
import charlotte.satellite.WinAPITools;

public class Fortewave {
	private PostOfficeBox _rPob; // closed 確認用
	private PostOfficeBox _wPob;

	public Fortewave(String ident) throws Exception {
		this(ident, ident);
	}

	public Fortewave(String rIdent, String wIdent) throws Exception {
		if(rIdent == null) {
			throw new NullPointerException("rIdent");
		}
		if(wIdent == null) {
			throw new NullPointerException("wIdent");
		}
		_rPob = new PostOfficeBox(rIdent);
		_wPob = new PostOfficeBox(wIdent);
	}

	public synchronized void clear() throws Exception {
		if(_rPob == null) {
			throw new Exception("already closed");
		}
		_rPob.clear();
		_wPob.clear();
	}

	public synchronized void send(Object sendObj) throws Exception {
		if(sendObj == null) {
			throw new NullPointerException("sendObj");
		}
		if(_rPob == null) {
			throw new Exception("already closed");
		}
		byte[] sendData = new Serializer(sendObj).getBytes();
		_wPob.send(sendData);
	}

	public synchronized Object recv(long millis) throws Exception {
		if(millis < 0) {
			throw new Exception("millis lt 0");
		}
		if(WinAPITools.INFINITE < millis) {
			throw new Exception("millis gt max");
		}
		if(_rPob == null) {
			throw new Exception("already closed");
		}
		byte[] recvData = _rPob.recv(millis);

		if(recvData == null) {
			return null;
		}
		Object recvObj = new Deserializer(recvData).next();
		return recvObj;
	}

	public synchronized void close() {
		if(_rPob != null) {
			_rPob.close();
			_wPob.close();
			_rPob = null;
			_wPob = null;
		}
	}
}
