package charlotte_test.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.DebugTools;
import charlotte.tools.MathTools;
import charlotte.tools.Merging;
import charlotte.tools.StringTools;

public class MergingTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() {
		for(int c = 0; c < 10000; c++) {
			System.out.println("[" + c + "]");

			List<String> lOnly = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);
			List<String> both  = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);
			List<String> rOnly = DebugTools.makeRandLines(StringTools.hexadecimal, MathTools.random(0, 100), 32, 32);

			daburase(lOnly);
			daburase(both);
			daburase(rOnly);

			List<String> l = new ArrayList<String>();
			List<String> r = new ArrayList<String>();

			l.addAll(lOnly);
			l.addAll(both);

			r.addAll(rOnly);
			r.addAll(both);

			ArrayTools.shuffle(l);
			ArrayTools.shuffle(r);

			List<String> outBoth = new ArrayList<String>();

			Merging.merge(l, r, outBoth, null, StringTools.comp);

			ArrayTools.sort(lOnly, StringTools.comp);
			ArrayTools.sort(both,  StringTools.comp);
			ArrayTools.sort(rOnly, StringTools.comp);
			ArrayTools.sort(l, StringTools.comp);
			ArrayTools.sort(r, StringTools.comp);
			ArrayTools.sort(outBoth, StringTools.comp);

			if(ArrayTools.isSame(lOnly, l, StringTools.comp) == false) throw null; // bug
			if(ArrayTools.isSame(rOnly, r, StringTools.comp) == false) throw null; // bug
			if(ArrayTools.isSame(both, outBoth, StringTools.comp) == false) throw null; // bug
		}
	}

	private static void daburase(List<String> list) {
		if(MathTools.random(2) == 0) {
			int num = MathTools.random(list.size() * 3 + 1);

			for(int c = 0; c < num; c++) {
				list.add(list.get(c));
			}
		}
	}
}
