package charlotte.tools;

import java.util.HashMap;
import java.util.Map;

public class StringIndexOf {
	private Map<Character, Integer> _map = new HashMap<Character, Integer>();

	public StringIndexOf(String str) {
		for(int index = str.length() - 1; 0 <= index; index--) {
			_map.put(str.charAt(index), index);
		}
	}

	public int get(char chr) {
		Integer ret = _map.get(chr);

		if(ret == null) {
			return -1;
		}
		return ret;
	}
}
