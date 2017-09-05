package bluetears.test08.white.yellow.blue;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;

public class BlueAlter02 implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		req.getHeaderFields().put("x-alter", req.getHeaderFields().get("x-alter") + ", BlueAlter02");
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		// noop
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
