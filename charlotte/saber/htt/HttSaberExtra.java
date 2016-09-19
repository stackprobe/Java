package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaberExtra extends Closeable {
	public void flame(HttSaberRequest req);
	public void flame(HttSaberRequest req, HttSaberResponse res);
}
