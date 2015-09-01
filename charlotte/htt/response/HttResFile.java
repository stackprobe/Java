package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;
import charlotte.tools.ByteWriter;
import charlotte.tools.ExtToContentType;
import charlotte.tools.FileTools;

public class HttResFile implements HttResponse {
	private File _file;
	private String _charset;

	public HttResFile(File file) {
		this(file, null);
	}

	public HttResFile(File file, String charset) {
		_file = file;
		_charset = charset;
	}

	@Override
	public String getHTTPVersion() throws Exception {
		return "HTTP/1.1";
	}

	@Override
	public int getStatusCode() throws Exception {
		return 200;
	}

	@Override
	public String getReasonPhrase() throws Exception {
		return "OK";
	}

	@Override
	public void writeHeaderFields(Map<String, String> dest) throws Exception {
		dest.put("Content-Type", getContentType());
	}

	private String getContentType() throws Exception {
		String ret = ExtToContentType.getContentType(FileTools.getExt(_file.getCanonicalPath()));

		if(_charset != null) {
			ret += "; charset=" + _charset;
		}
		return ret;
	}

	@Override
	public File getBodyPartFile() throws Exception {
		return _file;
	}

	@Override
	public void writeBodyPart(ByteWriter dest) throws Exception {
		// noop
	}
}
