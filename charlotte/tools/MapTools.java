package charlotte.tools;

import java.util.Map;
import java.util.TreeMap;

public class MapTools {
	public static <V> Map<String, V> create() {
		return new TreeMap<String, V>(StringTools.comp);
	}

	public static <V> Map<String, V> createIgnoreCase() {
		return new TreeMap<String, V>(StringTools.compIgnoreCase);
	}
}
