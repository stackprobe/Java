package bluetears.test.nest1.nest2.nest3;

import java.io.IOException;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.tools.BlockBuffer;
import charlotte.tools.StringTools;

public class NestAlter implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) {
		// noop
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) {
		try {
			BlockBuffer buff = new BlockBuffer();

			buff.bindAdd("NestAlter\r\n{\r\n\t".getBytes(StringTools.CHARSET_ASCII));
			buff.bindAdd(res.loadBody());
			buff.bindAdd("}\r\n".getBytes(StringTools.CHARSET_ASCII));

			res.setBody(buff.getBytes());
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void close() throws IOException {
		// noop
	}
}
