package charlotte.flowertact;

import charlotte.satellite.Deserializer;
import charlotte.satellite.Serializer;
import charlotte.satellite.WinAPITools;

public class Fortewave {
	private PostOfficeBox _rPob;
	private PostOfficeBox _wPob;

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

	public synchronized void clear() {
		_rPob.clear();
		_wPob.clear();
	}

	public synchronized void send(Object sendObj) throws Exception {
		if(sendObj == null) {
			throw new NullPointerException("sendObj");
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
		byte[] recvData = _rPob.recv(millis);

		if(recvData == null) {
			return null;
		}
		Object recvObj = new Deserializer(recvData).next();
		return recvObj;
	}
}
