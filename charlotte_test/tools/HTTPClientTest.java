package charlotte_test.tools;

import charlotte.tools.HTTPClient;
import charlotte.tools.StringTools;

public class HTTPClientTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void test01() throws Exception {
		HTTPClient hc = new HTTPClient("https://msdn.microsoft.com/ja-jp/subscriptions/downloads/hh442898.aspx");
		hc.setProxy("192.168.237.18", 3128);
		//hc.get();
		hc.head();
		byte[] bResBody = hc.getResBody();
		String resBody = new String(bResBody, StringTools.CHARSET_UTF8);

		System.out.println("resBody: " + resBody);
		System.out.println("HTTPVersion: " + hc.getResHTTPVersion());
		System.out.println("status: " + hc.getResStatus());
		System.out.println("reasonPhrase: " + hc.getResReasonPhrase());
	}
}
