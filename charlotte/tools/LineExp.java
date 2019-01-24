package charlotte.tools;

import java.util.List;

/**
 * original == https://github.com/stackprobe/Factory/blob/master/Common/LineExp.c
 *
 */
public class LineExp {
	private static StringIndexOf CHR_SET = new StringIndexOf(
			StringTools.CONTROLCODE +
			StringTools.ASCII_SPC +
			StringTools.HAN_KATAKANA +
			StringTools.ZEN_PUNCT +
			StringTools.ZEN_DIGIT +
			StringTools.ZEN_ALPHA +
			StringTools.zen_alpha +
			StringTools.ZEN_HIRAGANA +
			StringTools.ZEN_KATAKANA
			);
	private String _format;

	public void setFormat(String format) {
		_format = format;
	}

	public boolean check(String line) {
		return check(_format, line);
	}

	public boolean checkIgnoreCase(String line) {
		return checkIgnoreCase(_format, line);
	}

	public static boolean check(String format, String line) {
		int fp = 0;
		int lp = 0;
		int p;
		String tmpl;
		List<String> lnums;
		int nummin;
		int nummax;
		String rngs = null;
		int i;

		for(; ; fp++, lp++) {
			if(format.length() <= fp) {
				if(lp < line.length()) {
					return false;
				}
				break;
			}
			if(format.charAt(fp) == '/') {
				fp++;
				if(format.charAt(fp) != '/' && format.charAt(fp) != '<') throw new RuntimeException("format error");

				if(format.charAt(fp) != line.charAt(lp)) {
					return false;
				}
			}
			else if(format.charAt(fp) == '<') {
				fp++;
				p = format.indexOf('>', fp);
				if(p == -1) throw new RuntimeException("format error");

				tmpl = format.substring(fp, p);
				lnums = StringTools.tokenize(tmpl, ",");

				if(lnums.size() == 1) {
					lnums.add(0, ""); // <r> to <,r>
				}
				if(lnums.size() == 2) {
					lnums.add(1, lnums.get(0)); // <n,r> to <n,n,r>
				}
				if(lnums.size() != 3) throw new RuntimeException("format error");

				nummin = IntTools.toInt(lnums.get(0), 0, Integer.MAX_VALUE, 0);
				nummax = IntTools.toInt(lnums.get(1), 0, Integer.MAX_VALUE, 0);
				rngs = lnums.get(2);

				if(nummax == 0) {
					nummax = Integer.MAX_VALUE;
				}
				if(nummax < nummin) throw new RuntimeException("format error");

				fp = p + 1;

				for(i = 0; ; i++) {
					if(nummin <= i) {
						if(check(format.substring(fp), line.substring(lp))) {
							break;
						}
					}
					if(line.length() <= lp || isMatch(line.charAt(lp), rngs) == false) {
						return false;
					}
					if(nummax <= i) {
						return false;
					}
					lp++;
				}
			}
			else if(format.charAt(fp) != line.charAt(lp)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isMatch(char chr, String rngs) {
		int[] pp = new int[1];

		// rngs == "" -> 全マッチ
		if(rngs.length() == 0) {
			return true;
		}
		for(pp[0] = 0; pp[0] < rngs.length(); ) {
			char min;
			char max;

			min = getRngChar(rngs, pp);
			max = getRngChar(rngs, pp);

			if(max < min) throw new RuntimeException("format error");

			int imin = CHR_SET.get(min);
			if(imin == -1) throw new RuntimeException("format error");
			int imax = CHR_SET.get(max);
			if(imax == -1) throw new RuntimeException("format error");
			int ichr = CHR_SET.get(chr);

			if(min <= ichr && ichr <= max) {
				return true;
			}
		}
		return false;
	}

	private static char getRngChar(String rngs, int[] pp) {
		char chr = rngs.charAt(pp[0]);

		if(chr == '/') {
			pp[0]++;
			chr = rngs.charAt(pp[0]);

			if(chr != '/' && chr != 'G' && chr != 'M') throw new RuntimeException("format error");

			if(chr == 'G') {
				chr = '>';
			}
			else if(chr == 'M') {
				chr = ',';
			}
		}
		pp[0]++;
		return chr;
	}

	public static boolean checkIgnoreCase(String format, String line) {
		return check(format.toLowerCase(), line.toLowerCase());
	}
}
