package bluetears.test07.active;

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

		res.getHeaderFields().put("Content-Type", "text/plain; charset=UTF-8");
		res.setBody(("x-alter: [" +
			req.getHeaderFields().get("x-alter")
			+ "]").getBytes(StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
