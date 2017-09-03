package bluetears.test02;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberLily;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class DefaultPage implements HttSaberLily {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
		res.setBody("<html><body><h1>DefaultPage works!</h1></body></html>".getBytes(StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
