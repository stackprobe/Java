package evergarden.xxxtools.test;

import java.util.ArrayList;
import java.util.List;

import evergarden.xxxtools.IList;

public class IListTest {
	public static void main(String[] args) {
		try {
			List<String> la = new ArrayList<String>();
			List<String> lb = new ArrayList<String>();
			List<String> lc = new ArrayList<String>();

			la.add("a1");
			la.add("a2");
			la.add("a3");

			lb.add("b1");
			lb.add("b2");
			lb.add("b3");

			lc.add("c1");
			lc.add("c2");
			lc.add("c3");

			for(String str : new IList<String>("ABC", "DEF", "GHI")
					.add("aaa", "bbb", "ccc")
					.add(new String[]{ "1", "2", "3" })
					.add(la).add(lb).add(lc)) {
				System.out.println(str);
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
