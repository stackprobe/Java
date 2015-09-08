package charlotte.htt;

import java.io.File;
import java.util.Map;

public interface HttResponse {
	public String getHTTPVersion() throws Exception;
	public int getStatusCode() throws Exception;
	public String getReasonPhrase() throws Exception;
	public void writeHeaderFields(Map<String, String> dest) throws Exception;
	public File getBodyPartFile() throws Exception;
	public byte[] getBodyPart() throws Exception;
}
