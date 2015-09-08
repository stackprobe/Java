package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;

public class HttRes404 implements HttResponse {
	@Override
	public String getHTTPVersion() throws Exception {
		return "HTTP/1.1";
	}

	@Override
	public int getStatusCode() throws Exception {
		return 404;
	}

	@Override
	public String getReasonPhrase() throws Exception {
		return "Not Found";
	}

	@Override
	public void writeHeaderFields(Map<String, String> dest) throws Exception {
		// noop
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
