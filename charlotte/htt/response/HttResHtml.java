package charlotte.htt.response;

import java.io.File;
import java.util.Map;

import charlotte.htt.HttResponse;
import charlotte.tools.StringTools;

public class HttResHtml implements HttResponse {
	private String _htmlText;
	private String _charset;

	public HttResHtml() {
		this("<html><body><h1>Happy tea time!</h1></body></html>");
	}

	public HttResHtml(String htmlText) {
		this(htmlText, StringTools.CHARSET_UTF8);
	}

	public HttResHtml(String htmlText, String charset) {
		_htmlText = htmlText;
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
		dest.put("Content-Type", "text/html; charset=" + _charset);
	}

	@Override
	public File getBodyPartFile() throws Exception {
		return null;
	}

	@Override
	public byte[] getBodyPart() throws Exception {
		return _htmlText.getBytes(_charset);
	}
}
