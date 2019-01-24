package charlotte.saber.htt;

import java.io.File;
import java.util.Map;

import charlotte.tools.FileTools;
import charlotte.tools.ObjectMap;

public class HttSaberResponse {
	private String _httpVersion;
	private int _statusCode;
	private String _reasonPhrase;
	private Map<String, String> _headerFields;
	private File _bodyFile;
	private byte[] _body;

	public String getHTTPVersion() {
		return _httpVersion;
	}

	public void setHTTPVersion(String httpVersion) {
		_httpVersion = httpVersion;
	}

	public int getStatusCode() {
		return _statusCode;
	}

	public void setStatusCode(int statusCode) {
		_statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return _reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		_reasonPhrase = reasonPhrase;
	}

	public Map<String, String> getHeaderFields() {
		return _headerFields;
	}

	public void setHeaderFields(Map<String, String> headerFields) {
		_headerFields = headerFields;
	}

	public File getBodyFile() {
		return _bodyFile;
	}

	public void setBodyFile(File bodyFile) {
		_bodyFile = bodyFile;
		_body = null;
	}

	public byte[] getBody() {
		return _body;
	}

	public byte[] loadBody() throws Exception {
		if(_bodyFile != null) {
			_body = FileTools.readAllBytes(_bodyFile);
			_bodyFile = null;
		}
		return _body;
	}

	public void setBody(byte[] body) {
		_body = body;
		_bodyFile = null;
	}

	private ObjectMap _arguments = null;

	public ObjectMap arguments() {
		if(_arguments == null) {
			_arguments = ObjectMap.create();
		}
		return _arguments;
	}
}
