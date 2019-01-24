package bluetears.test08.white.yellow;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class Niiiii implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
		res.setBody(("<html><body><h1>Niiiii works!</h1>" +
				"-- 当たり前だけど、ここでは配下の Alter (BlueAlter01～03.java, LightBlueAlter.java) は実行されないよ。<br/>" +
				"</body></html>").getBytes(StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
