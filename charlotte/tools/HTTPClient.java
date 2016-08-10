package charlotte.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPClient {
	private String _url = "http://localhost/";
	private Map<String, String> _headerFields = new HashMap<String, String>();
	private byte[] _body = null; // null == GET, not null == POST
	private int _connectTimeoutMillis = 20000;
	private int _readTimeoutMillis = 60000;
	private String _proxyDomain = null; // null -> no proxy
	private int _proxyPortNo = 8080;
	private boolean _head; // true -> HEAD, false -> GET or POST
	private String _resBodyFile = null;

	public HTTPClient() {
	}

	public HTTPClient(String url) {
		_url = url;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setAuthorization(String user, String password) throws Exception {
		String plain = user + ":" + password;
		String enc = Base64.getString(plain.getBytes(StringTools.CHARSET_UTF8));
		setHeaderField("Authorization", "Basic " + enc);
	}

	public void setHeaderField(String name, String value) {
		_headerFields.put(name, value);
	}

	public void setBody(byte[] body) {
		_body = body;
	}

	public void setResBodyFile(String file) {
		_resBodyFile = file;
	}

	public void setConnectTimeoutMillis(int millis) {
		_connectTimeoutMillis = millis;
	}

	public void setReadTimeoutMillis(int millis) {
		_readTimeoutMillis = millis;
	}

	public void setProxy(String domain, int portNo) {
		_proxyDomain = domain;
		_proxyPortNo = portNo;
	}

	public void setHeadFlag(boolean head) {
		_head = head;
	}

	public void head() throws Exception {
		_head = true;
		_body = null;
		perform();
	}

	public void get() throws Exception {
		post(null);
	}

	public void post(byte[] body) throws Exception {
		_head = false;
		_body = body;
		perform();
	}

	public void perform() throws Exception {
		HttpURLConnection con = null;

		try {
			{
				URL url = new URL(_url);

				if(_proxyDomain == null) {
					con = (HttpURLConnection)url.openConnection();
				}
				else {
					SocketAddress sa = new InetSocketAddress(_proxyDomain, _proxyPortNo);
					Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);

					con = (HttpURLConnection)url.openConnection(proxy);
				}
			}

			if(_head) {
				con.setRequestMethod("HEAD");
			}
			else if(_body == null) {
				con.setRequestMethod("GET");
			}
			else {
				con.setRequestMethod("POST");
			}
			con.setDoOutput(_body != null);
			con.setInstanceFollowRedirects(false);
			con.setConnectTimeout(_connectTimeoutMillis);
			con.setReadTimeout(_readTimeoutMillis);

			for(String name : _headerFields.keySet()) {
				con.setRequestProperty(name, _headerFields.get(name));
			}
			con.connect();

			if(_body != null) {
				OutputStream os = null;

				try {
					os = con.getOutputStream();
					os.write(_body);
				}
				finally {
					FileTools.close(os);
				}
			}
			//_resFirstLine = con.getHeaderField(0);

			for(String name : con.getHeaderFields().keySet()) {
				List<String> values = con.getHeaderFields().get(name);
				String value = StringTools.join(" ", values);

				if(name == null) {
					_resFirstLine = value;
				}
				else {
					_resHeaderFields.put(name, value);
				}
			}

			{
				InputStream is = null;

				try {
					is = con.getInputStream();

					if(_resBodyFile == null) {
						_resBody = FileTools.readToEnd(is);
					}
					else {
						FileTools.writeToEnd(is, _resBodyFile);
					}
				}
				finally {
					FileTools.close(is);
				}
			}
		}
		finally {
			try {
				con.disconnect();
			}
			catch(Throwable e) {
				// ignore
			}
		}
	}

	private String _resFirstLine = null;
	private Map<String, String> _resHeaderFields = new HashMap<String, String>();
	private byte[] _resBody = null;

	public String getResFirstLine() {
		return _resFirstLine;
	}

	private String[] _resFirstLineTokens;

	public String[] getResFirstLineTokens() {
		if(_resFirstLineTokens == null) {
			_resFirstLineTokens = StringTools.tokenize(_resFirstLine, " ").toArray(new String[3]);
		}
		return _resFirstLineTokens;
	}

	public String getResHTTPVersion() {
		return getResFirstLineTokens()[0];
	}

	public int getResStatus() {
		return Integer.parseInt(getResFirstLineTokens()[1]);
	}

	public String getResReasonPhrase() {
		return getResFirstLineTokens()[2];
	}

	public Map<String, String> getResHeaderFields() {
		return _resHeaderFields;
	}

	public byte[] getResBody() {
		return _resBody;
	}
}
