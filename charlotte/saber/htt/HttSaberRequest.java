package charlotte.saber.htt;

import java.util.Map;

public class HttSaberRequest {
	private String _method;
	private String _urlString;
	private String _httpVersion;
	private Map<String, String> _headerFields;
	private byte[] _body;

	public String getMethod() {
		return _method;
	}

	public void setMethod(String method) {
		_method = method;
	}

	public String getUrlString() {
		return _urlString;
	}

	public void setUrlString(String urlString) {
		_urlString = urlString;
	}

	public String getHTTPVersion() {
		return _httpVersion;
	}

	public void setHTTPVersion(String httpVersion) {
		_httpVersion = httpVersion;
	}

	public Map<String, String> getHeaderFields() {
		return _headerFields;
	}

	public void setHeaderFields(Map<String, String> headerFields) {
		_headerFields = headerFields;
	}

	public byte[] getBody() {
		return _body;
	}

	public void setBody(byte[] body) {
		_body = body;
	}
}
