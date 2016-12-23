package cecilia.htt;

import charlotte.saber.htt.HttArtoria;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.satellite.MutexObject;

public class HttAlcott extends HttArtoria {
	private static final String IDENT = "{b84b2deb-cbd7-47e4-9a41-22b7d9f66bd8}";
	private static MutexObject _mo;

	public static boolean lock() {
		if(_mo == null) {
			_mo = new MutexObject(IDENT + "_m");

			if(_mo.waitOne(0) == false) {
				_mo = null;
				return false;
			}
		}
		return true;
	}

	public static void unlock() {
		if(_mo != null) {
			_mo.release();
			_mo = null;
		}
	}

	@Override
	protected Package getRoot(HttSaberRequest req) throws Exception {
		return bluetears.R.class.getPackage();
	}
}
