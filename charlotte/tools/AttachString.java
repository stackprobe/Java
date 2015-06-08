package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class AttachString {
	public static AttachString i = new AttachString();

	public AttachString() {
		this("\t\r\n ", "trns");
	}

	public AttachString(String decChrs, String encChrs) {
		this(':', '$', '.', decChrs, encChrs);
	}

	public AttachString(char delimiter, char escapeChr, char escapeDelimiter) {
		this(delimiter, escapeChr, escapeDelimiter, "", "");
	}

	private char _delimiter;
	private EscapeString _es;

	public AttachString(char delimiter, char escapeChr, char escapeDelimiter, String decChrs, String encChrs) {
		this(delimiter, new EscapeString(
				decChrs + delimiter,
				escapeChr,
				encChrs + escapeDelimiter
				));
	}

	public AttachString(char delimiter, EscapeString es) {
		_delimiter = delimiter;
		_es = es;
	}

	public String untokenize(List<String> tokens) {
		List<String> tmp = new ArrayList<String>();

		for(String token : tokens) {
			tmp.add(_es.encode(token));
		}
		tmp.add("");
		return StringTools.join("" + _delimiter, tmp);
	}

	public List<String> tokenize(String str) {
		List<String> ret = new ArrayList<String>();

		for(String token : StringTools.tokenize(str, "" + _delimiter)) {
			ret.add(_es.decode(token));
		}
		ret.remove(ret.size() - 1);
		return ret;
	}
}
