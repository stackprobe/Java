package cecilia.htt;

import bluetears.BlueTearsHome;
import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberRequest;

public class HttAlcott extends HttArtoria {
	@Override
	protected Package getRoot(HttSaberRequest req) throws Exception {
		return BlueTearsHome.class.getPackage();
	}
}
