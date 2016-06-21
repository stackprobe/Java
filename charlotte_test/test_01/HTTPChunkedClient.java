package charlotte_test.test_01;

import java.io.OutputStream;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPResponse;
import charlotte.tools.IntTools;
import charlotte.tools.MathTools;
import charlotte.tools.SockClient;
import charlotte.tools.StringTools;

public class HTTPChunkedClient {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		perform(
				"localhost",
				80,
				"/",
				FileTools.readAllText("C:/var/20141007_GBCTunnel_Log.txt", StringTools.CHARSET_SJIS).getBytes(StringTools.CHARSET_UTF8)
				);
	}

	private static void test02() throws Exception {
		chunkOfDeath(
				"localhost",
				80,
				"/"
				);
	}

	private static void perform(String domain, int portNo, String path, byte[] body) throws Exception {
		SockClient client = null;

		try {
			client = new SockClient(domain, portNo, 20000, 20000);
			OutputStream ws = client.getOutputStream();

			write(ws, "POST " + path + " HTTP/1.1\r\n");
			write(ws, "Host " + domain + ":" + portNo + "\r\n");
			write(ws, "Content-Type: text/plain; charset=UTF-8\r\n");
			write(ws, "Transfer-Encoding: chunked\r\n");
			write(ws, "\r\n");

			int rPos = 0;

			while(rPos < body.length) {
				int nextSize = MathTools.random(1, body.length - rPos);

				System.out.println("nextSize: " + nextSize); // test

				if(MathTools.random(2) == 0) {
					write(ws, chunkSz2String(nextSize) + "\r\n");
				}
				else {
					write(ws, chunkSz2String(nextSize) + "; dummy-chunked-extension; dummy-chunke-key = 'dummy-chunked-value'\r\n");
				}
				ws.write(ArrayTools.getBytes(body, rPos, nextSize));
				write(ws, "\r\n");
				rPos += nextSize;
			}

			if(MathTools.random(2) == 0) {
				write(ws, "0\r\n");
			}
			else {
				write(ws, "0; dummy-chunked-extension; dummy-chunke-key = 'dummy-chunked-value'\r\n");
			}
			while(MathTools.random(2) == 0) {
				write(ws, "dummy-trailer-header: dummy-trailer-header-value\r\n");
			}
			write(ws, "\r\n");

			HTTPResponse res = new HTTPResponse(client.getInputStream());

			System.out.println("resBody: " + new String(res.getBody(), StringTools.CHARSET_ASCII));
		}
		finally {
			FileTools.close(client);
		}
	}

	private static String chunkSz2String(int size) {
		return IntTools.toHex(size); // こっちが正解
		//return "" + size;
	}

	private static void write(OutputStream ws, String str) throws Exception {
		write(ws, str, StringTools.CHARSET_ASCII);
	}

	private static void write(OutputStream ws, String str, String charset) throws Exception {
		ws.write(str.getBytes(charset));
	}

	private static void chunkOfDeath(String domain, int portNo, String path) throws Exception {
		SockClient client = null;

		try {
			client = new SockClient(domain, portNo, 20000, 20000);
			OutputStream ws = client.getOutputStream();

			write(ws, "POST " + path + " HTTP/1.1\r\n");
			write(ws, "Host " + domain + ":" + portNo + "\r\n");
			write(ws, "Content-Type: text/plain; charset=US-ASCII\r\n");
			write(ws, "Transfer-Encoding: chunked\r\n");
			write(ws, "\r\n");

			int nextSizeMax = 1000;

			for(; ; ) {
				int nextSize = MathTools.random(1, nextSizeMax);
				System.out.println("nextSize: " + nextSize + " (nextSizeMax: " + nextSizeMax + ")"); // test
				nextSizeMax = Math.min((int)(nextSizeMax * 1.1), IntTools.IMAX);

				if(MathTools.random(2) == 0) {
					write(ws, chunkSz2String(nextSize) + "\r\n");
				}
				else {
					write(ws, chunkSz2String(nextSize) + "; dummy-chunked-extension; dummy-chunke-key = 'dummy-chunked-value'\r\n");
				}
				for(int c = 0; c < nextSize; c++) {
					ws.write(nextDummyChar());
				}
				write(ws, "\r\n");
			}
		}
		finally {
			FileTools.close(client);
		}
	}

	private static String _dummyChrs = StringTools.ASCII + "\r\n";
	private static int _dummyChrIndex = -1;

	private static int nextDummyChar() {
		_dummyChrIndex++;
		_dummyChrIndex %= _dummyChrs.length();

		return _dummyChrs.charAt(_dummyChrIndex);
	}
}
