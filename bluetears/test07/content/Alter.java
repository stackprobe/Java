package bluetears.test07.content;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.saber.htt.HttSaberX;
import charlotte.tools.FileTools;
import charlotte.tools.RunnableEx;
import charlotte.tools.StringTools;

public class Alter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) throws HttSaberX {
		// noop
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) throws HttSaberX {
		try {
			// charset を付ける。
			res.getHeaderFields().put("Content-Type", "text/plain; charset=UTF-8");

			// コンテンツの場合は、bodyFile なので注意してね。
			res.setBody(("wrapped by content/Alter.java {" +
					FileTools.readAllText(res.getBodyFile().getCanonicalPath(), StringTools.CHARSET_UTF8)
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
