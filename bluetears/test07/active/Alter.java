package bluetears.test07.active;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.RunnableEx;
import charlotte.tools.StringTools;

public class Alter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		req.getHeaderFields().put("x-alter", "added by active/Alter.java");
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		try {
			res.setBody(("wrapped by active/Alter.java {" +
					new String(res.getBody(), StringTools.CHARSET_UTF8)
					+ "}").getBytes(StringTools.CHARSET_UTF8));
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
