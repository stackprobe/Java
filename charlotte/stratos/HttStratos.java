package charlotte.stratos;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;

public class HttStratos implements HttService {
	public HttStratos(String rootPackage) {
	}

	@Override
	public boolean interlude() throws Exception {
		return false;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		return null;
	}
}
