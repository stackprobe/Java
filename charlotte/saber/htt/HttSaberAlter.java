package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaberAlter extends Closeable {
	public void flame(HttSaberRequest req) throws HttSaberX;
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX;
}
