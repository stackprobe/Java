package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;

public class HttRes302 implements HttResponse {
	private String _location;

	public HttRes302(String location) {
		_location = location;
	}

	@Override
	public String getHTTPVersion() throws Exception {
		return "HTTP/1.1";
	}

	@Override
	public int getStatusCode() throws Exception {
		return 301;
	}

	@Override
	public String getReasonPhrase() throws Exception {
		return "Found";
	}

	@Override
	public void writeHeaderFields(Map<String, String> dest) throws Exception {
		dest.put("Location", _location);
	}

	@Override
	public File getBodyPartFile() throws Exception {
		return null;
	}

	@Override
	public byte[] getBodyPart() throws Exception {
		return null;
	}
}
