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
	private String _basePath;
	private OutputStream _writer;
	private InputStream _reader;

	public FilingCase3Client(String domain, int portno, String basePath) throws Exception {
		_client = new SockClient(domain, portno, 30000, 0);
		_basePath = basePath;
		_writer = _client.getOutputStream();
		_reader = _client.getInputStream();
	}

	public byte[] get(String path) throws Exception {
		send("GET", path);
		byte[] ret = read64();
		readLineCheck("/GET/e");
		return ret;
	}

	public int post(String path, byte[] data) throws Exception {
		send("POST", path, data);
		readLineCheck("/POST/e");
		return 1;
	}

	public byte[] getPost(String path, byte[] data) throws Exception {
		send("GET-POST", path, data);
		byte[] ret = read64();
		readLineCheck("/GET/e");
		readLineCheck("/GET-POST/e");
		return ret;
	}

	public List<String> list(String path) throws Exception {
		send("LIST", path);
		return readListYen();
	}

	public int delete(String path) throws Exception {
		send("DELETE", path);
		readLineCheck("/DELETE/e");
		return 1;
	}

	private void send(String command, String path) throws Exception {
		send(command, path, new byte[0]);
	}

	private void send(String command, String path, byte[] data) throws Exception {
		writeLine(command);
		writeLine(FileTools.oNormYen(_basePath + "/" + path));
		writeLine("" + data.length);
		_writer.write(data);
		writeLine("/SEND/e");
		_writer.flush();
	}

	private void writeLine(String line) throws Exception {
		writeLine(line.getBytes(StringTools.CHARSET_SJIS));
	}

	private void writeLine(byte[] bLine) throws Exception {
		_writer.write(bLine);
		_writer.write(0x0d); // cr
		_writer.write(0x0a); // lf
	}

	private List<String> readListYen() throws Exception {
		List<String> list = new ArrayList<String>();

		for(; ; ) {
			String lPathYen = readLine();

			if("/LIST/e".equals(lPathYen)) {
				break;
			}
			String lPath = lPathYen.replace('\\', '/');

			list.add(lPath);
		}
		return list;
	}

	private void readLineCheck(String line) throws Exception {
		if(line.equals(readLine()) == false) {
			throw new Exception("Can not read " + line);
		}
	}

	private String readLine() throws Exception {
		return new String(read(), StringTools.CHARSET_SJIS);
	}

	private byte[] read() throws Exception {
		return read(IntTools.toInt(read(4), 0));
	}

	private byte[] read64() throws Exception {
		return read(IntTools.toInt(read(8), 0));
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
