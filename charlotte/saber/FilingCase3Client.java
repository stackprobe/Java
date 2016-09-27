package charlotte.saber;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.SockClient;
import charlotte.tools.StringTools;

public class FilingCase3Client implements Closeable {
	private SockClient _client;
	private OutputStream _writer;
	private InputStream _reader;

	public FilingCase3Client(String domain, int portno) throws Exception {
		_client = new SockClient(domain, portno, 30000, 0);
		_writer = _client.getOutputStream();
		_reader = _client.getInputStream();
	}

	public byte[] get(String path) throws Exception {
		send("GET", path);
		return read();
	}

	public void post(String path, byte[] data) throws Exception {
		send("POST", path, data);
	}

	public List<String> list(String path) throws Exception {
		send("LIST", path);
		return readLines();
	}

	public void delete(String path) throws Exception {
		send("DELETE", path);
	}

	private void send(String command, String path) throws Exception {
		send(command, path, new byte[0]);
	}

	private void send(String command, String path, byte[] data) throws Exception {
		writeLine(command);
		writeLine(path);
		writeLine("" + data.length);
		writeLine(data);
	}

	private void writeLine(String line) throws Exception {
		writeLine(line.getBytes(StringTools.CHARSET_SJIS));
	}

	private void writeLine(byte[] bLine) throws Exception {
		_writer.write(bLine);
		_writer.write(0x0d); // cr
		_writer.write(0x0a); // lf
	}

	private List<String> readLines() throws Exception {
		List<String> lines = new ArrayList<String>();

		for(; ; ) {
			String line = readLine();

			if(line.length() == 0) {
				break;
			}
			lines.add(line);
		}
		return lines;
	}

	private String readLine() throws Exception {
		return new String(read(), StringTools.CHARSET_SJIS);
	}

	private byte[] read() throws Exception {
		return read(IntTools.toInt(read(4), 0));
	}

	private byte[] read(int size) throws Exception {
		return FileTools.readToSize(_reader, size);
	}

	@Override
	public void close() throws IOException {
		if(_client != null) {
			FileTools.close(_client);
			_client = null;
		}
	}
}
