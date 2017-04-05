package bluetears.test.nest1.nest2;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class NestPage implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/plain; charset=US-ASCII");
		res.setBody(
				("This is Nest2Page. (alters=" + req.getHeaderFields().get("Alters") + ")\r\n")
				.getBytes(StringTools.CHARSET_ASCII)
				);

		return res;
	}

	@Override
	public void close() throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}
}
