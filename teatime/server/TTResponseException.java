package teatime.server;

public class TTResponseException extends Exception {
	private TTResponse _res;

	public TTResponseException(TTResponse res) {
		_res = res;
	}

	public TTResponse getResponse() {
		return _res;
	}
}
