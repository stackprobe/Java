package cecilia.htt;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberRequest;

public class HttAlcott extends HttArtoria {
	public static final String IDENT = "{b84b2deb-cbd7-47e4-9a41-22b7d9f66bd8}";

	@Override
	protected Package getRoot(HttSaberRequest req) throws Exception {
		return bluetears.R.class.getPackage();
	}
}
