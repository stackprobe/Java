package charlotte.saber.htt;

public interface HttSaberAlter {
	public void alternate(HttSaberRequest req) throws Exception;
	public void alternate(HttSaberResponse res) throws Exception;
}
