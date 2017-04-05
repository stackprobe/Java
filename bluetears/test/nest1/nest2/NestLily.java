package bluetears.test.nest1.nest2;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberLily;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.StringTools;

public class NestLily implements HttSaberLily {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception, HttSaberX {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.setBody("<html><body><h1>Nest2Lily!</h1></body></html>".getBytes(StringTools.CHARSET_ASCII));

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
