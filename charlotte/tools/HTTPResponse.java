package charlotte.tools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
	private final int CR = 0x0d;
	private final int LF = 0x0a;
	private final int HEADER_LINE_LENMAX = 65536;

	private InputStream _rs;
	private String _firstLine;
	private Map<String, String> _headerFields = new HashMap<String, String>();
	private int _contentLength;
	private boolean _chunked;
	private byte[] _body;

	public HTTPResponse(InputStream rs) throws Exception {
		_rs = rs;
		readFirstLine();
		readHeaderFields();
		checkHeaderFields();
		readBody();
		_rs = null;
	}

	private String readLine() throws Exception {
		byte[] buff = new byte[HEADER_LINE_LENMAX];
		int wPos = 0;

		for(; ; ) {
			int chr = _rs.read();

			if(chr == -1) {
				throw new Exception("文字列の途中で終端に到達しました。");
			}
			if(chr == CR) {
				chr = _rs.read();

				if(chr != LF) {
					throw new Exception("改行の2文字目がLFではありません。");
				}
				break;
			}
			buff[wPos] = (byte)chr;
			wPos++;
		}
		//System.out.println("wPos: " + wPos); // test
		return new String(buff, 0, wPos, StringTools.CHARSET_ASCII);
	}

	private void readFirstLine() throws Exception {
		_firstLine = readLine();
	}

	private void readHeaderFields() throws Exception {
		List<String> lines = new ArrayList<String>();

		for(; ; ) {
			String line = readLine();

			if(line.length() == 0) {
				break;
			}
			char firstChr = line.charAt(0);

			line = line.trim();

			if(firstChr == '\t' || firstChr == ' ') {
				lines.set(
						lines.size() - 1,
						lines.get(lines.size() - 1) + " " + line
						);
			}
			else {
				lines.add(line);
			}
		}
		for(String line : lines) {
			int index = line.indexOf(':');
			String name = line.substring(0, index);
			String value = line.substring(index + 1);

			name = name.trim();
			name = name.toLowerCase();
			value = value.trim();

			_headerFields.put(name, value);
		}
	}

	private void checkHeaderFields() {
		String sConLen = _headerFields.get("content-length");
		String sTrnEnc = _headerFields.get("transfer-encoding");

		if(sConLen != null) {
			_contentLength = Integer.parseInt(sConLen);
		}
		if(sTrnEnc != null) {
			sTrnEnc = sTrnEnc.toLowerCase();

			if(sTrnEnc.contains("chunked")) {
				_chunked = true;
			}
		}
	}

	private byte[] readBytes(int size) throws Exception {
		byte[] buff = new byte[size];

		for(int wPos = 0; wPos < size; wPos++) {
			int chr = _rs.read();

			if(chr == -1) {
				throw new Exception("バイナリデータの途中で終端に到達しました。");
			}
			buff[wPos] = (byte)chr;
		}
		return buff;
	}

	private void readBody() throws Exception {
		if(_chunked) {
			List<byte[]> parts = new ArrayList<byte[]>();
			int totalSize = 0;

			for(; ; ) {
				String line = readLine();
				int extPos = line.indexOf(';');

				if(extPos != -1) {
					line = line.substring(0, extPos); // ignore chunked-extension
				}
				line = line.trim();
				int partSize = Integer.parseInt(line, 16);

				//System.out.println("partSize: " + partSize); // test

				if(partSize == 0) {
					break;
				}
				byte[] part = readBytes(partSize);
				parts.add(part);
				totalSize += partSize;

				_rs.read(); // CR
				_rs.read(); // LF
			}
			while(1 <= readLine().length()); // ignore chunked-footer
			_body = new byte[totalSize];
			int wPos = 0;

			for(byte[] part : parts) {
				System.arraycopy(part, 0, _body, wPos, part.length);
				wPos += part.length;
			}
		}
		else {
			_body = readBytes(_contentLength);
		}
	}

	public String getFirstLine() {
		return _firstLine;
	}

	public Map<String, String> getHeaderFields() {
		return _headerFields;
	}

	public byte[] getBody() {
		return _body;
	}
}
