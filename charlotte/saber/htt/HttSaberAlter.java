package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaberAlter extends Closeable {
	public void alter(HttSaberRequest req) throws Exception;
	public void alter(HttSaberResponse res) throws Exception;
}
