package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;
import charlotte.tools.ExtToContentType;
import charlotte.tools.FileTools;

public class HttResFileImage implements HttResponse {
	private byte[] _fileData;
	private String _virPath;
	private String _charset;

	public HttResFileImage(byte[] fileData, String virPath) {
		this(fileData, virPath, null);
	}

	public HttResFileImage(byte[] fileData, String virPath, String charset) {
		_fileData = fileData;
		_virPath = virPath;
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
		String ret = ExtToContentType.getContentType(FileTools.getExt(_virPath));

		if(_charset != null) {
			ret += "; charset=" + _charset;
		}
		return ret;
	}

	@Override
	public File getBodyPartFile() throws Exception {
		return null;
	}

	@Override
	public byte[] getBodyPart() throws Exception {
		return _fileData;
	}
}
