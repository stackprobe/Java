package charlotte.tools;

import java.util.Set;
import java.util.TreeSet;

public class SetTools {
	public static Set<String> create() {
		return new TreeSet<String>(StringTools.comp);
	}

	public static Set<String> createIgnoreCase() {
		return new TreeSet<String>(StringTools.compIgnoreCase);
	}
}
