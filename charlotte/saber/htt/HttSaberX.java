package charlotte.saber.htt;

public class HttSaberX extends Throwable {
	private HttSaberResponse _res;

	public HttSaberX(HttSaberResponse res) {
		_res = res;
	}

	public HttSaberResponse getRes() {
		return _res;
	}
}
