package bluetears.test09.fresh_green.orange;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.RunnableEx;
import charlotte.tools.StringTools;

public class OrangeAlter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		System.out.println("OrangeAlter.java flame-req entered");

		if(req.getUrlString().contains("throwSaberX=OrangeReq")) {
			HttSaberResponse res = HttArtoria.i().createResponse();

			res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
			try {
				res.setBody("<html><body>throw @ OrangeAlter-req</body></html>".getBytes(StringTools.CHARSET_UTF8));
			}
			catch(Throwable e) {
				throw RunnableEx.re(e);
			}

			throw new HttSaberX(res);
		}
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		System.out.println("OrangeAlter.java flame-res entered");

		if(req.getUrlString().contains("throwSaberX=OrangeRes")) {
			res = HttArtoria.i().createResponse();

			res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
			try {
				res.setBody("<html><body>throw @ OrangeAlter-res</body></html>".getBytes(StringTools.CHARSET_UTF8));
			}
			catch(Throwable e) {
				throw RunnableEx.re(e);
			}

			throw new HttSaberX(res);
		}
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
