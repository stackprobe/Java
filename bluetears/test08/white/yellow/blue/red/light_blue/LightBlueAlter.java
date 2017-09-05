package bluetears.test08.white.yellow.blue.red.light_blue;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;

public class LightBlueAlter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		req.getHeaderFields().put("x-alter", req.getHeaderFields().get("x-alter") + ", LightBlueAlter");
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
