package evergarden.codeiq;

import charlotte.tools.IntTools;
import charlotte.tools.StringTools;

public class J1551 {
	public static void main(String[] args) {
		try {
			perform();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void perform() {
		for(int n = 2; ; n++) {
			for(int d = 1; d < n; d++) {
				if(tryDivide(n, d)) {
					System.out.println(n + " / " + d);
					return;
				}
			}
		}
	}

	private static boolean tryDivide(int n, int d) {
		int a = n / d;
		String sa = "" + a;
		StringBuffer digs = new StringBuffer();

		digs.append("" + n);
		digs.append("" + d);
		digs.append("" + a);

		for(int index = 0; index < sa.length(); index++) {
			int e = StringTools.DIGIT.indexOf(sa.charAt(index));
			e *= d;
			digs.append("" + e);
			int scale = IntTools.pow(10, sa.length() - 1 - index);
			e *= scale;
			n -= e;
			digs.append("" + n);
		}
		String sDigs = digs.toString();

		for(char dig : StringTools.DIGIT.toCharArray()) {
			if(StringTools.contains(sDigs, dig) == false) {
				return false;
			}
		}
		return true;
	}
}
