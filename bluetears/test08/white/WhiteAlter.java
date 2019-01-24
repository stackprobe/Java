package bluetears.test08.white;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.RunnableEx;

public class WhiteAlter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		req.getHeaderFields().put("x-alter", "WhiteAlter");
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		try {
			String contentType = res.getHeaderFields().get("Content-Type");

			if("text/plain".equals(contentType)) {
				res.getHeaderFields().put("Content-Type", "text/plain; charset=UTF-8");
			}
		}
		catch(Exception e) {
			throw RunnableEx.re(e);
		}
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
