package charlotte.tools;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
	private String _domain = "localhost";
	private int _portNo = 80;
	private String _path = "/";
	private Map<String, String> _headerFields = new HashMap<String, String>();
	private byte[] _body = null; // null -> GET, not null -> POST
	private int _connectTimeoutMillis = 20000; // 0 -> infinite
	private int _soTimeoutMillis = 60000; // 0 -> infinite
	private String _proxyDomain = null; // null -> no proxy
	private int _proxyPortNo = -1;
	private boolean _head;

	public HTTPRequest() {
	}

	public HTTPRequest(String url) {
		setUrl(url);
	}

	public void setUrl(String url) {
		if(url.startsWith("http://")) {
			url = url.substring(7);
		}
		int index = url.indexOf('/');

		if(index != -1) {
			_domain = url.substring(0, index);
			_path = url.substring(index);
		}
		else {
			_domain = url;
			_path = "/";
		}
		index = _domain.indexOf(':');

		if(index != -1) {
			_portNo = Integer.parseInt(_domain.substring(index + 1));
			_domain = url.substring(0, index);
		}
		else {
			_portNo = 80;
		}
	}

	public void setHeaderField(String name, String value) {
		_headerFields.put(name, value);
	}

	public void setBody(byte[] body) {
		_body = body;
	}

	public void setConnectTimeoutMillis(int millis) {
		_connectTimeoutMillis = millis;
	}

	public void setSoTimeoutMillis(int millis) {
		_soTimeoutMillis = millis;
	}

	public void setProxy(String domain, int portNo) {
		_proxyDomain = domain;
		_proxyPortNo = portNo;
	}

	public void head() {
		_head = true;
	}

	public HTTPResponse perform() throws Exception {
		if(_portNo == 80) {
			setHeaderField("Host", _domain);
		}
		else {
			setHeaderField("Host", _domain + ":" + _portNo);
		}
		if(_body != null) {
			setHeaderField("Content-Length", "" + _body.length);
		}
		SockClient client;

		if(_proxyDomain == null) {
			client = new SockClient(_domain, _portNo, _connectTimeoutMillis, _soTimeoutMillis);
		}
		else {
			client = new SockClient(_proxyDomain, _proxyPortNo, _connectTimeoutMillis, _soTimeoutMillis);
		}
		try {
			OutputStream ws = client.getOutputStream();

			if(_head) {
				write(ws, "HEAD ");
			}
			else if(_body == null) {
				write(ws, "GET ");
			}
			else {
				write(ws, "POST ");
			}
			if(_proxyDomain == null) {
				write(ws, _path);
			}
			else if(_proxyPortNo == 80) {
				write(ws, "http://" + _domain + "/" + _path);
			}
			else {
				write(ws, "http://" + _domain + ":" + _portNo + "/" + _path);
			}
			write(ws, " HTTP/1.1\r\n");

			for(String name : _headerFields.keySet()) {
				String value = _headerFields.get(name);

				write(ws, name);
				write(ws, ": ");
				write(ws, value);
				write(ws, "\r\n");
			}
			write(ws, "\r\n");

			if(_body != null) {
				ws.write(_body);
			}
			ws.flush();
			return new HTTPResponse(client.getInputStream(), _head);
		}
		finally {
			FileTools.close(client);
		}
	}

	private static void write(OutputStream ws, String str) throws Exception {
		ws.write(str.getBytes(StringTools.CHARSET_ASCII));
	}
}
