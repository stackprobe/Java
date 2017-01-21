package bluetears.test.nest1.nest2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.saber.htt.HttSaberRequest;
import charlotte.saber.htt.HttSaberResponse;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class Alter1 implements HttSaberAlter {
	@Override
	public void flame(HttSaberRequest req) {
		String alters = req.getHeaderFields().get("Alters");

		if(alters == null) {
			alters = "";
		}
		alters += "[Alter1]";

		req.getHeaderFields().put("Alters", alters);
	}

	@Override
	public void flame(HttSaberRequest req, HttSaberResponse res) {
		try {
			String text = new String(res.getBody(), StringTools.CHARSET_ASCII);
			List<String> lines = FileTools.textToLines(text);
			List<String> dest = new ArrayList<String>();

			dest.add("Alter1");
			dest.add("{");

			for(String line : lines) {
				dest.add("\t" + line);
			}
			dest.add("}");

			res.setBody(FileTools.linesToText(dest).getBytes(StringTools.CHARSET_ASCII));
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