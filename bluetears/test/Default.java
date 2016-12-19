package bluetears.test;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberLily;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.tools.FileTools;

public class Default implements HttSaberLily {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.getHeaderFields().put("Content-Type", "text/html; charset=UTF-8");
		res.setBody(FileTools.readToEnd(Default.class.getResource("Default.html")));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
