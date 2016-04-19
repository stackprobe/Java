package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.HTTPRequest;
import charlotte.tools.HTTPResponse;
import charlotte.tools.StringTools;

public class HTTPRequestTest {
	public static void main(String[] args) {
		try {
			main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		{
			HTTPRequest req = new HTTPRequest(
					//"http://ja.wikipedia.org/wiki/Hypertext_Transfer_Protocol"
					"http://192.168.222.101:50080/"
					);
			HTTPResponse res = req.get();
			byte[] resBody = res.getBody();

			FileTools.writeAllBytes("C:/temp/HTTPRequest_resBody.txt", resBody);

			String resText = new String(
					resBody,
					//StringTools.CHARSET_UTF8
					StringTools.CHARSET_SJIS
					);

			System.out.println(resText);
		}

		{
			HTTPRequest req = new HTTPRequest(
					//"http://ja.wikipedia.org/wiki/Hypertext_Transfer_Protocol"
					"http://192.168.222.101:50080/"
					);
			HTTPResponse res = req.head();
			byte[] resBody = res.getBody();

			if(resBody != null) {
				throw new Exception("ng");
			}

			//FileTools.writeAllBytes("C:/temp/HTTPRequest_resBody.txt", resBody);

			/*
			String resText = new String(
					resBody,
					//StringTools.CHARSET_UTF8
					StringTools.CHARSET_SJIS
					);
					*/

			//System.out.println(resText);
		}
	}
}
