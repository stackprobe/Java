package bluetears.test06;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class ActivePage implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
		res.setBody("<html><body><h1>HttSaberX を例外として投げても応答できる！</h1></body></html>".getBytes(StringTools.CHARSET_UTF8));

		throw new HttSaberX(res);
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
