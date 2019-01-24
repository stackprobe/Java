package bluetears.test08.white.yellow.blue.red.green;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;

public class GreenAlter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		System.out.println("GeelAlter frame(req) called !!!");
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		System.out.println("GeelAlter frame(req, res) called !!!");
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
