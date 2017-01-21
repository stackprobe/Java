package bluetears.test.nest1;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberLily;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.tools.StringTools;

public class NestLily implements HttSaberLily {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.setBody("<html><body><h1>NestLily!</h1></body></html>".getBytes(StringTools.CHARSET_ASCII));

		return res;
	}

	@Override
	public void close() throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}
}