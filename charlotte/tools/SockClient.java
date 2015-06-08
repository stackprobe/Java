package charlotte.tools;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SockClient implements Closeable {
	private Socket _sock;

	public SockClient(String domain, int portNo, int connectTimeoutMillis, int soTimeoutMillis) throws Exception {
		_sock = new Socket();
		_sock.setSoTimeout(soTimeoutMillis);
		InetAddress ia = InetAddress.getByName(domain);
		InetSocketAddress isa = new InetSocketAddress(ia, portNo);
		_sock.connect(isa, connectTimeoutMillis);
	}

	public OutputStream getOutputStream() throws Exception {
		return _sock.getOutputStream();
	}

	public InputStream getInputStream() throws Exception {
		return _sock.getInputStream();
	}

	@Override
	public void close() throws IOException {
		_sock.close();
	}
}
