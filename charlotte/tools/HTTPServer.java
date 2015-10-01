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
		public String url;
		public String verPart;

		public void perform(Socket sock, HTTPServer server) throws Exception {
			sock.setSoTimeout(server._soTimeoutMillis);

			req = new HTTPResponse(sock.getInputStream());

			{
				List<String> tokens = StringTools.tokenize(req.getFirstLine(), " ");

				method = tokens.get(0);
				url = tokens.get(1);
				verPart = tokens.get(2);
			}

			server.recved(this);
			sendPrep();
			send(sock.getOutputStream());
		}

		public String resStatus = "200";
		public Map<String, String> resHeaderFields = new HashMap<String, String>();
		public byte[] resBody; // null -> no body

		private void sendPrep() {
			resHeaderFields.put("Server", "beer");
			resHeaderFields.put("Connection", "close");

			if(resBody != null) {
				resHeaderFields.put("Content-Length", "" + resBody.length);
			}
		}

		private void send(OutputStream ws) throws Exception {
			write(ws, "HTTP/1.1 ");
			write(ws, resStatus);
			write(ws, " Drink HUB ALE\r\n");

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
}
