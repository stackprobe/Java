package charlotte.htt;

public interface HttService {
	public boolean interlude() throws Exception;
	public HttResponse service(HttRequest req) throws Exception;
}
