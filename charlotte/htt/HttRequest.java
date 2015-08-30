package charlotte.htt;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import charlotte.satellite.ObjectList;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class HttRequest {
	private String _method;
	private String _urlString;
	private String _httpVersion;
	private Map<String, String> _headerFields;
	private String _bodyPartFile;

	public HttRequest(ObjectList rawData) throws Exception {
		int c = 1;

		_method = new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII);
		_urlString = new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII);
		_httpVersion = new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII);

		{
			int count = Integer.parseInt(new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII));

			_headerFields = new HashMap<String, String>();

			for(int index = 0; index < count; index++) {
				String key = new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII);
				String value = new String((byte[])rawData.get(c++), StringTools.CHARSET_ASCII);

				_headerFields.put(key, value);
			}
		}

		_bodyPartFile = new String((byte[])rawData.get(c++), StringTools.CHARSET_SJIS);
	}

	public String getMethod() {
		return _method;
	}

	public String getUrlString() {
		return _urlString;
	}

	public URL getUrl() throws MalformedURLException {
		return new URL(_urlString);
	}

	public String getHTTPVersion() {
		return _httpVersion;
	}

	public Map<String, String> getHeaderFields() {
		return _headerFields;
	}

	public File getBodyPartFile() {
		return new File(_bodyPartFile);
	}

	public byte[] getBodyPart() throws Exception {
		return FileTools.readAllBytes(_bodyPartFile);
	}
}
