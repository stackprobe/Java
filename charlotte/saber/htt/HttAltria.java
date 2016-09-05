package charlotte.saber.htt;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;

public abstract class HttAltria implements HttService {
	@Override
	public boolean interlude() throws Exception {
		return false;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		return null;
	}

	protected abstract Package getRootPackage(HttSaberRequest req);
}
