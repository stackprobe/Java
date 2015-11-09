package charlotte.tools;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HTTPServer extends SockServer {
	public HTTPServer() {
		this(80);
	}

	public HTTPServer(int portNo) {
		super(portNo);
	}

	private int _soTimeoutMillis = 60000; // 0 -> infinite

	public void setSoTimeoutMillis(int millis) {
		_soTimeoutMillis = millis;
	}

	@Override
	protected void connectionTh(Socket sock) throws Exception {
		new Connection().perform(sock, this);
	}

	public static class Connection {
		public HTTPResponse req;
		public String method;
		public String path;
		public String httpVersion;

		public void perform(Socket sock, HTTPServer server) throws Exception {
			sock.setSoTimeout(server._soTimeoutMillis);

			req = new HTTPResponse(sock.getInputStream());

			{
				List<String> tokens = StringTools.tokenize(req.getFirstLine(), " ");

				method = tokens.get(0);
				path = tokens.get(1);
				httpVersion = tokens.get(2);
			}

			server.recved(this);
			sendPrep();
			send(sock.getOutputStream());
		}

		public String resStatus = "200";
		public Map<String, String> resHeaderFields = new HashMap<String, String>();
		public byte[] resBody; // null -> no body

		private void sendPrep() {
			resHeaderFields.put("Server", HTTPServer.class.getName());
			resHeaderFields.put("Connection", "close");

			if(resBody != null) {
				resHeaderFields.put("Content-Length", "" + resBody.length);
			}
		}

		private void send(OutputStream ws) throws Exception {
			write(ws, "HTTP/1.1 ");
			write(ws, resStatus);
			write(ws, " Performed\r\n");

			for(String name : resHeaderFields.keySet()) {
				String value = resHeaderFields.get(name);

				write(ws, name);
				write(ws, ": ");
				write(ws, value);
				write(ws, "\r\n");
			}
			write(ws, "\r\n");

			if(resBody != null) {
				ws.write(resBody);
			}
		}

		private static void write(OutputStream ws, String str) throws Exception {
			ws.write(str.getBytes(StringTools.CHARSET_ASCII));
		}
	}

	protected abstract void recved(Connection con) throws Exception;

	public static Map<String, String> parseQuery(Connection con) throws Exception {
		return parseQuery(con, StringTools.CHARSET_UTF8);
	}

	public static Map<String, String> parseQuery(Connection con, String charset) throws Exception {
		byte[] body = con.req.getBody();
		String query;

		if(body.length == 0) {
			String path = con.path;
			int index = path.indexOf('?');
			query = path.substring(index + 1);
		}
		else {
			query = new String(body, StringTools.CHARSET_ASCII);
		}
		List<String> parts = StringTools.tokenize(query, "&");
		Map<String, String> dest = new HashMap<String, String>();

		for(String part : parts) {
			List<String> tokens = StringTools.tokenize(part, "=");

			if(tokens.size() == 2) {
				String key = tokens.get(0);
				String value = tokens.get(1);

				key = decodeUrl(key, charset);
				value = decodeUrl(value, charset);

				key = key.toUpperCase(); // XXX

				dest.put(key, value);
			}
		}
		return dest;
	}

	public static String decodeUrl(String str, String charset) throws Exception {
		ByteBuffer buff = new ByteBuffer();

		for(int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);

			if(chr == '%') {
				buff.add(StringTools.hex(str.substring(index + 1, index + 3))[0]);
				index += 2;
			}
			else {
				if(chr == '+') {
					chr = ' ';
				}
				buff.add(new String(new char[] { chr }).getBytes()[0]);
			}
		}
		byte[] block = buff.getBytes();
		String ret = new String(block, charset);
		return ret;
	}
}
