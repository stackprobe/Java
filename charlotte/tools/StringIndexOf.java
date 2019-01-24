package charlotte.tools;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class StringIndexOf {
	private Map<Character, Integer> _map = new TreeMap<Character, Integer>(new Comparator<Character>() {
		@Override
		public int compare(Character a, Character b) {
			return (int)a.charValue() - (int)b.charValue();
		}
	});

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
