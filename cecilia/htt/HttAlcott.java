package cecilia.htt;

import java.io.IOException;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaber;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;

public class HttAlcott extends HttArtoria {
	@Override
	protected Package getRoot(HttSaberRequest req) throws Exception {
		return Package.getPackage("bluetears");
	}

	@Override
	public HttSaber getSaber404(HttSaberRequest req) throws Exception {
		return new HttSaber() {
			@Override
			public HttSaberResponse doRequest(HttSaberRequest req) throws Exception {
				HttSaberResponse res = createResponse();
				res.setStatusCode(404);
				return res;
			}

			@Override
			public void close() throws IOException {
				// noop
			}
		};
	}
}
