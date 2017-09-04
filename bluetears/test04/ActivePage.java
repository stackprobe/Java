package bluetears.test04;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class ActivePage implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");

		StringBuffer buff = new StringBuffer();

		buff.append("<html><body><h1>ActivePage works!</h1>");
		buff.append(new String(
				FileTools.readToEnd(ActivePage.class.getResource("ActivePage.txt")),
				StringTools.CHARSET_UTF8
				));
		buff.append("</body></html>");

		res.setBody(buff.toString().getBytes(StringTools.CHARSET_UTF8));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
