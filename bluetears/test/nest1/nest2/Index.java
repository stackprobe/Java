package bluetears.test.nest1.nest2;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;

public class Index implements HttSaber {
	@Override
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception {
		HttSaberResponse res = HttArtoria.i().createResponse();

		res.setBody("A".getBytes());

		return res;
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
