package bluetears.test08.white.yellow.blue.red.light_blue;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class LightBlue implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/plain"); // WhiteAlter.java が charset=UTF-8 付けてくれるはず。
		res.setBody(("x-alter: [" +
			req.getHeaderFields().get("x-alter")
			+ "] -- いろはにほへと　漢字　ホゲホゲ").getBytes(StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
