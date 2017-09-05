package bluetears.test09.fresh_green.orange.pink.purple;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class Black implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		System.out.println("Blacnk.java entered");

		if(req.getUrlString().contains("throwSaberX=Black")) {
			HttSaberResponse res = HttArtoria.i().createResponse();

			res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
			res.setBody(StringTools.getBytes("<html><body>throw @ Black</body></html>", StringTools.CHARSET_UTF8));

			throw new HttSaberX(res); // OrangeAlfter.frame(req, res); は呼ばれないよ。
		}

		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
		res.setBody(StringTools.getBytes("<html><body><h1>makoto-kun works!</h1></body></html>", StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
