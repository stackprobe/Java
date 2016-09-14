package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaber extends Closeable {
	public HttSaberResponse doRequest(HttSaberRequest req) throws Exception;
}
