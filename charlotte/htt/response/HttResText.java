package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;
import charlotte.tools.StringTools;

public class HttResText implements HttResponse {
	private String _text;
	private String _charset;

	public HttResText() {
		this("Happy tea time!");
	}

	public HttResText(String htmlText) {
		this(htmlText, StringTools.CHARSET_UTF8);
	}

	public HttResText(String text, String charset) {
		_text = text;
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
		dest.put("Content-Type", "text/plain; charset=" + _charset);
	}

	@Override
	public File getBodyPartFile() throws Exception {
		return null;
	}

	@Override
	public byte[] getBodyPart() throws Exception {
		return _text.getBytes(_charset);
	}
}
