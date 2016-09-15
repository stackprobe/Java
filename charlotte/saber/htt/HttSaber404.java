package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaber404 extends Closeable {
	public HttSaberResponse getResponse(HttSaberRequest req);
}
