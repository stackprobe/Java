package charlotte.saber.htt;

import java.io.Closeable;

public interface HttSaberExtra extends Closeable {
	public boolean needToMaintenance();
	public void maintenance();
}
