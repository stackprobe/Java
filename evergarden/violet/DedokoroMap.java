package evergarden.violet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.HugeQueue;
import charlotte.tools.MapTools;
import charlotte.tools.StringTools;

public class DedokoroMap {
	public static void main(String[] args) {
		try {
			test01("C:/tmp/Dedokoro.txt");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01(String file) throws Exception {
		Map<String, List<String>> ls = MapTools.<List<String>>create(); // r -> l
		Map<String, List<String>> rs = MapTools.<List<String>>create(); // l -> r

		for(String line : FileTools.readAllLines(file, StringTools.CHARSET_SJIS)) {
			int p = line.indexOf(' ');

			String l = line.substring(0, p);
			String r = line.substring(p + 1);

			add(ls, r, l);
			add(rs, l, r);
		}
		writeToFile("C:/tmp/Dedokoro_File2Class.txt", ls);
		writeToFile("C:/tmp/Dedokoro_Class2File.txt", rs);
	}

	private static void add(Map<String, List<String>> m, String k, String v) {
		List<String> vs = m.get(k);

		if(vs == null) {
			vs = new ArrayList<String>();
			m.put(k, vs);
		}
		vs.add(v);
	}

	private static void writeToFile(String file, Map<String, List<String>> m) throws Exception {
		HugeQueue dest = new HugeQueue();
		try {
			for(String k : sort(m.keySet())) {
				dest.add(k);

				for(String v : sort(m.get(k))) {
					dest.add("\t" + v);
				}
				dest.add("");
			}
			FileTools.writeAllLines(file, dest, StringTools.CHARSET_SJIS);
		}
		finally {
			FileTools.close(dest);
		}
	}

	private static List<String> sort(Collection<String> src) {
		List<String> dest = new ArrayList<String>();

		for(String line : src) {
			dest.add(line);
		}
		ArrayTools.sort(dest, StringTools.comp);
		return dest;
	}
}
