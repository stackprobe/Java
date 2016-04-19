package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class MultiPartFormData {
	private byte[] _body;
	private String _charset;
	private int _rPos;
	private List<Part> _parts = new ArrayList<Part>();
	private String _boundary;
	private byte[] _crlfBoundary;

	public MultiPartFormData(byte[] body, String charset) throws Exception {
		_body = body;
		_charset = charset;
		_rPos = 0;

		_boundary = readLine(StringTools.CHARSET_ASCII);
		_crlfBoundary = ("\r\n" + _boundary).getBytes(StringTools.CHARSET_ASCII);

		do {
			Part part = new Part();

			readHeader(part);
			part.body = readBody();

			_parts.add(part);
		}
		while(isContinue());
	}

	private void readHeader(Part part) throws Exception {
		for(; ; ) {
			String line = readLine(_charset);

			if("".equals(line)) {
				break;
			}
			readHeaderLine(part, line);
		}
	}

	private int _linePos;

	private void readHeaderLine(Part part, String line) throws Exception {
		// Content-Description:
		if(StringTools.containsIgnoreCase(line, "Content-Description") == false) {
			return;
		}
		// form-data;
		if(StringTools.containsIgnoreCase(line, "form-data") == false) {
			return;
		}
		_linePos = 0;

		for(; ; ) {
			int namePos = StringTools.indexOfIgnoreCase(line, "name", _linePos);
			int filenamePos = StringTools.indexOfIgnoreCase(line, "filename", _linePos);

			if(namePos != -1 && (filenamePos == -1 || namePos < filenamePos)) {
				part.name = readHeaderValue(line, namePos);
			}
			else if(filenamePos != -1) {
				part.filename = readHeaderValue(line, filenamePos);
			}
			else {
				break;
			}
		}
		_linePos = -1;
	}

	private String readHeaderValue(String line, int fromIndex) throws Exception {
		int p = line.indexOf('"', fromIndex);
		p++;
		int q = line.indexOf('"', p);

		if(q == -1) {
			throw new Exception("format error");
		}
		_linePos = q + 1;

		return line.substring(p, q);
	}

	private byte[] readBody() {
		ByteBuffer buff = new ByteBuffer();

		while(ArrayTools.isSame(_body, _rPos, _crlfBoundary, 0, _crlfBoundary.length) == false) {
			buff.add(readChar());
		}
		return buff.getBytes();
	}

	private boolean isContinue() {
		// ? CR
		if(readChar() == (byte)0x2d) {
			readChar(); // LF
			return true;
		}
		return false;
	}

	private String readLine(String charset) throws Exception {
		ByteBuffer buff = new ByteBuffer();

		for(; ; ) {
			byte chr = readChar();

			// ? CR
			if(chr == (byte)0x0d) {
				break;
			}
			buff.add(chr);
		}
		readChar(); // LF
		return new String(buff.getBytes(), charset);
	}

	private byte readChar() {
		return _body[_rPos++];
	}

	public List<Part> getParts() {
		return _parts;
	}

	public ObjectMap getObjectMap() throws Exception {
		ObjectMap ret = ObjectMap.createIgnoreCase();

		for(Part part : _parts) {
			String name = part.name;

			if(part.filename != null) {
				ret.add(name, part.body);
				ret.add(name + ".filename", part.filename);
			}
			else {
				ret.add(name, new String(part.body, _charset));
			}
		}
		return ret;
	}

	public static class Part {
		public String name;
		public String filename;
		public byte[] body;
	}
}
