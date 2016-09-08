package charlotte.saber.htt;

public interface HttSaber {
	public boolean interlude() throws Exception;
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception;
}
