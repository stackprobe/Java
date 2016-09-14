package charlotte.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class StringTools {
	public static List<String> tokenize(String str, String delimiters) {
		return tokenize(str, delimiters, false, false);
	}

	public static List<String> meaningTokenize(String str, String meanings) {
		return tokenize(str, meanings, true, true);
	}

	public static List<String> tokenize(String str, String delimiters, boolean meaningFlag, boolean ignoreEmpty) {
		List<String> tokens = new ArrayList<String>();
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if((delimiters.indexOf(chr) == -1) == meaningFlag) {
				tokens.add(buff.toString());
				buff = new StringBuffer();
			}
			else {
				buff.append(chr);
			}
		}
		tokens.add(buff.toString());

		if(ignoreEmpty)
			removeEmpty(tokens);

		return tokens;
	}

	public static void removeEmpty(List<String> list) {
		int wPos = 0;

		for(String str : list) {
			if(str != null && 1 <= str.length()) {
				list.set(wPos, str);
				wPos++;
			}
		}
		while(wPos < list.size()) {
			list.remove(list.size() - 1);
		}
	}

	public static String getUUID() {
		return "{" + UUID.randomUUID() + "}";
	}

	public static String replace(String str, String fromPtn, String toPtn, int count) {
		while(1 <= count) {
			str = str.replace(fromPtn, toPtn);
			count--;
		}
		return str;
	}

	public static final String DIGIT = "0123456789";
	public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String alpha = "abcdefghijklmnopqrstuvwxyz";
	public static final String HEXADECIMAL = "0123456789ABCDEF";
	public static final String hexadecimal = "0123456789abcdef";
	public static final String OCTODECIMAL = "01234567";
	public static final String octodecimal = "01234567";
	public static final String BINADECIMAL = "01";

	public static List<String> numericTokenize(String str) {
		return meaningTokenize(str, DIGIT);
	}

	public static String zPad(int value, int minlen) {
		return zPad("" + value, minlen);
	}

	public static String zPad(String str, int minlen) {
		return lPad(str, minlen, '0');
	}

	public static String lPad(String str, int minlen, char pad) {
		if(str.length() < minlen) {
			return StringTools.repeat(pad, minlen - str.length()) + str;
		}
		return str;
	}

	private static String repeat(char chr, int count) {
		StringBuffer buff = new StringBuffer();

		while(1 <= count) {
			buff.append(chr);
			count--;
		}
		return buff.toString();
	}

	public static boolean formatIs(String format, String ptn) {
		return format.equals(toFormat(ptn));
	}

	public static String toFormat(String str) {
		str = replaceChar(str, DIGIT, '9');
		str = replaceChar(str, ALPHA, 'A');
		str = replaceChar(str, alpha, 'a');

		return str;
	}

	public static boolean hexFormatIs(String format, String ptn) {
		return format.equals(toHexFormat(ptn));
	}

	public static String toHexFormat(String str) {
		str = replaceChar(str, HEXADECIMAL, 'f');
		str = replaceChar(str, hexadecimal, 'f');
		str = replaceChar(str, ALPHA, 'A');
		str = replaceChar(str, alpha, 'a');

		return str;
	}

	public static String replaceChar(String str, String fromChrs, char toChr) {
		for(char fromChr : fromChrs.toCharArray()) {
			str = str.replace(fromChr, toChr);
		}
		return str;
	}

	public static String replaceChar(String str, String fromChrs, String toChrs) {
		for(int index = 0; index < fromChrs.length(); index++) {
			str = str.replace(fromChrs.charAt(index), toChrs.charAt(index));
		}
		return str;
	}

	public static int indexOf(String str, String[] ptns) {
		return indexOf(str, ptns, 0);
	}

	public static int indexOf(String str, String[] ptns, int fromIndex) {
		int ret = Integer.MAX_VALUE;

		for(String ptn : ptns) {
			int i = str.indexOf(ptn, fromIndex);

			if(i != -1) {
				ret = Math.min(ret, i);
			}
		}
		if(ret == Integer.MAX_VALUE) {
			ret = -1;
		}
		return ret;
	}

	public static int lastIndexOf(String str, String[] ptns) {
		int ret = -1;

		for(String ptn : ptns) {
			ret = Math.max(ret, str.lastIndexOf(ptn));
		}
		return ret;
	}

	public static int lastIndexOfChar(String str, String chrs) {
		int ret = -1;

		for(char chr : chrs.toCharArray()) {
			ret = Math.max(ret, str.lastIndexOf(chr));
		}
		return ret;
	}

	public static String toHex(byte[] block) {
		return toHex(block, 0);
	}

	public static String toHex(byte[] block, int startPos) {
		return toHex(block, startPos, block.length - startPos);
	}

	/**
	 * バイト列 -> 16進文字列
	 * @param block バイト列
	 * @return 16進文字列
	 */
	public static String toHex(byte[] block, int startPos, int size) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < size; index++) {
			byte chr = block[startPos + index];

			buff.append(hexadecimal.charAt((chr & 0xf0) >> 4));
			buff.append(hexadecimal.charAt((chr & 0x0f) >> 0));
		}
		return buff.toString();
	}

	private static final StringIndexOf hexadecimal_i = new StringIndexOf(hexadecimal);

	/**
	 * 16進文字列 -> バイト列
	 * @param str 16進文字列
	 * @return バイト列
	 */
	public static byte[] hex(String str) {
		byte[] ret = new byte[str.length() / 2];

		str = str.toLowerCase();

		for(int index = 0; index < ret.length; index++) {
			int val =
				(hexadecimal_i.get(str.charAt(index * 2 + 0)) << 4) |
				(hexadecimal_i.get(str.charAt(index * 2 + 1)) << 0);

			ret[index] = (byte)val;
		}
		return ret;
	}

	public static final String CHARSET_ASCII = "US-ASCII";
	public static final String CHARSET_SJIS = "MS932";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_UTF16 = "UTF-16";
	public static final String CHARSET_UTF16BE = "UnicodeBigUnmarked";
	public static final String CHARSET_UTF16LE = "UnicodeLittleUnmarked";
	public static final String CHARSET_UTF32 = "UTF-32";

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String join(String separator, List<String> tokens) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < tokens.size(); index++) {
			if(1 <= index) {
				buff.append(separator);
			}
			buff.append(tokens.get(index));
		}
		return buff.toString();
	}

	public static String format(String str, String... prms) {
		for(String prm : prms) {
			int d = str.indexOf('$');

			if(d == -1) {
				break;
			}
			str = str.substring(0, d) + prm + str.substring(d + 1);
		}
		return str;
	}

	public static boolean hasSameChar(String str) {
		for(int r = 1; r < str.length(); r++) {
			for(int l = 0; l < r; l++) {
				if(str.charAt(l) == str.charAt(r)) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<String> random(String chrs, int minlen, int maxlen, int minsz, int maxsz) {
		return random(chrs, minlen, maxlen, MathTools.random(minsz, maxsz));
	}

	public static List<String> random(String chrs, int minlen, int maxlen, int size) {
		List<String> ret = new ArrayList<String>();

		for(int c = 0; c < size; c++) {
			ret.add(random(chrs, minlen, maxlen));
		}
		return ret;
	}

	public static String random(String chrs, int minlen, int maxlen) {
		return random(chrs, MathTools.random(minlen, maxlen));
	}

	public static String random(String chrs, int length) {
		StringBuffer buff = new StringBuffer();

		while(buff.length() < length) {
			buff.append(chrs.charAt(MathTools.random(chrs.length())));
		}
		return buff.toString();
	}

	public static boolean isSame(List<String> list1, List<String> list2) {
		return isSame(list1, list2, false);
	}

	public static boolean isSame(List<String> list1, List<String> list2, boolean ignoreCase) {
		if(list1 == null && list2 == null) {
			return true;
		}
		if(list1 == null || list2 == null) {
			return false;
		}
		if(list1.size() != list2.size()) {
			return false;
		}
		for(int index = 0; index < list1.size(); index++) {
			if(isSame(list1.get(index), list2.get(index), ignoreCase) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSame(String str1, String str2) {
		return isSame(str1, str2, false);
	}

	public static boolean isSame(String str1, String str2, boolean ignoreCase) {
		if(str1 == null && str2 == null) {
			return true;
		}
		if(str1 == null || str2 == null) {
			return false;
		}
		if(ignoreCase) {
			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();
		}
		return str1.equals(str2);
	}

	public static boolean contains(String[] arr, String target) {
		return ArrayTools.contains(arr, target, comp);
	}

	public static boolean contains(List<String> list, String target) {
		return ArrayTools.contains(list, target, comp);
	}

	public static boolean contains(String str, char chr) {
		return str.indexOf(chr) != -1;
	}

	public static boolean containsChar(String str, String chrs) {
		for(char chr : chrs.toCharArray()) {
			if(contains(str, chr)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsOnly(String str, String validChrs) {
		for(char chr : str.toCharArray()) {
			if(contains(validChrs, chr) == false) {
				return false;
			}
		}
		return true;
	}

	public static String toContainsOnly(String str, String validChrs) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if(contains(validChrs, chr)) {
				buff.append(chr);
			}
		}
		return buff.toString();
	}

	public static boolean isContainsOnly(String str, String validChrs) {
		return str.equals(toContainsOnly(str, validChrs));
	}

	public static String set(String str, int index, char chr) {
		return str.substring(0, index) + chr + str.substring(index + 1);
	}

	public static String insert(String str, int index, char chr) {
		return insert(str, index, "" + chr);
	}

	public static String insert(String str, int index, String ptn) {
		return str.substring(0, index) + ptn + str.substring(index);
	}

	public static String remove(String str, int index) {
		return remove(str, index, 1);
	}

	public static String remove(String str, int index, int count) {
		return str.substring(0, index) + str.substring(index + count);
	}

	public static boolean startsWith(String str, String ptn) {
		return startsWith(str, ptn, false);
	}

	public static boolean startsWithIgnoreCase(String str, String ptn) {
		return startsWith(str, ptn, true);
	}

	public static boolean startsWith(String str, String ptn, boolean ignoreCase) {
		if(str.length() < ptn.length()) {
			return false;
		}
		return isSame(str.substring(0, ptn.length()), ptn, ignoreCase);
	}

	public static boolean endsWith(String str, String ptn) {
		return endsWith(str, ptn, false);
	}

	public static boolean endsWithIgnoreCase(String str, String ptn) {
		return endsWith(str, ptn, true);
	}

	public static boolean endsWith(String str, String ptn, boolean ignoreCase) {
		if(str.length() < ptn.length()) {
			return false;
		}
		return isSame(str.substring(str.length() - ptn.length()), ptn, ignoreCase);
	}

	public static String removeStartsWith(String str, String ptn) {
		return removeStartsWith(str, ptn, false);
	}

	public static String removeStartsWithIgnoreCase(String str, String ptn) {
		return removeStartsWith(str, ptn, true);
	}

	public static String removeStartsWith(String str, String ptn, boolean ignoreCase) {
		if(startsWith(str, ptn, ignoreCase)) {
			return remove(str, 0, ptn.length());
		}
		return str;
	}

	public static String removeEndsWith(String str, String ptn) {
		return removeEndsWith(str, ptn, false);
	}

	public static String removeEndsWithIgnoreCase(String str, String ptn) {
		return removeEndsWith(str, ptn, true);
	}

	public static String removeEndsWith(String str, String ptn, boolean ignoreCase) {
		if(endsWith(str, ptn, ignoreCase)) {
			return remove(str, str.length() - ptn.length(), ptn.length());
		}
		return str;
	}

	public static Comparator<String> comp = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return a.compareTo(b);
		}
	};

	public static Comparator<String> compIgnoreCase = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return a.compareToIgnoreCase(b);
		}
	};

	public static int nCompare(String a, String b) {
		return nCompare(a, b, false);
	}

	public static int nCompareIgnoreCase(String a, String b) {
		return nCompare(a, b, true);
	}

	/**
	 * ANY_STRING < null
	 *
	 * @param a
	 * @param b
	 * @param ignoreCase
	 * @return
	 */
	public static int nCompare(String a, String b, boolean ignoreCase) {
		if(a == null) {
			if(b == null) {
				return 0;
			}
			return 1;
		}
		if(b == null) {
			return -1;
		}
		if(ignoreCase) {
			return a.compareToIgnoreCase(b);
		}
		return a.compareTo(b);
	}

	public static Comparator<String> nComp = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return nCompare(a, b);
		}
	};

	public static Comparator<String> nCompIgnoreCase = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			return nCompareIgnoreCase(a, b);
		}
	};

	public static String ASCII;
	public static String ASCII_SPC;
	public static String HAN_KATAKANA;
	public static String PUNCT;
	public static String CONTROLCODE;

	static {
		try {
			ASCII = new String(ArrayTools.byteSq(0x21, 0x7e), CHARSET_ASCII);
			ASCII_SPC = new String(ArrayTools.byteSq(0x20, 0x7e), CHARSET_ASCII);
			HAN_KATAKANA = new String(ArrayTools.byteSq(0xa1, 0xdf), CHARSET_SJIS);
			PUNCT = new String(ArrayTools.byteSq(0x21, 0x2f), CHARSET_ASCII) +
					new String(ArrayTools.byteSq(0x3a, 0x40), CHARSET_ASCII) +
					new String(ArrayTools.byteSq(0x5b, 0x60), CHARSET_ASCII) +
					new String(ArrayTools.byteSq(0x7b, 0x7e), CHARSET_ASCII);
			CONTROLCODE = new String(ArrayTools.byteSq(0x00, 0x1f), CHARSET_ASCII);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static final String ZEN_HIRAGANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん";
	public static final String ZEN_KATAKANA = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン";
	public static final String ZEN_DIGIT = "０１２３４５６７８９";
	public static final String ZEN_ALPHA = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ";
	public static final String zen_alpha = "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";

	public static String getRandString(int minlen, int maxlen) {
		return getRandString(minlen, maxlen, ASCII + ZEN_HIRAGANA + ZEN_KATAKANA);
	}

	public static String getRandJString(int minlen, int maxlen) {
		return getRandString(minlen, maxlen, ZEN_HIRAGANA + ZEN_KATAKANA);
	}

	public static String getRandString(int minlen, int maxlen, String validChrs) {
		StringBuffer buff = new StringBuffer();
		int count = MathTools.random(minlen, maxlen);

		for(int index = 0; index < count; index++) {
			buff.append(validChrs.charAt(MathTools.random(validChrs.length())));
		}
		return buff.toString();
	}

	public static final String S_TRUE = "true";
	public static final String S_FALSE = "false";

	public static String toString(boolean flag) {
		return flag ? S_TRUE : S_FALSE;
	}

	public static boolean toFlag(String str) {
		return StringTools.containsIgnoreCase(str, S_TRUE);
	}

	public static int indexOfIgnoreCase(String str, String ptn, int fromIndex) {
		for(int index = fromIndex; index + ptn.length() <= str.length(); index++) {
			String mid = str.substring(index, index + ptn.length());

			if(mid.equalsIgnoreCase(ptn)) {
				return index;
			}
		}
		return -1;
	}

	public static int indexOfIgnoreCase(String str, String ptn) {
		return indexOfIgnoreCase(str, ptn, 0);
	}

	public static boolean containsIgnoreCase(String str, String ptn) {
		return indexOfIgnoreCase(str, ptn) == -1;
	}

	public static String decodeUrl(String str, String charset) throws Exception {
		return HTTPServer.decodeUrl(str, charset);
	}

	public static String encodeUrl(String str, String charset) throws Exception {
		return HTTPServer.encodeUrl(str, charset);
	}

	public static Enclosed getEnclosed(String text, String bgnTag, String endTag) {
		Enclosed ret = new Enclosed();

		ret.text = text;
		ret.bgnBgn = text.indexOf(bgnTag);

		if(ret.bgnBgn == -1) {
			return null;
		}
		ret.bgnEnd = ret.bgnBgn + bgnTag.length();
		ret.endBgn = text.indexOf(endTag, ret.bgnEnd);

		if(ret.endBgn == -1) {
			return null;
		}
		ret.endEnd = ret.endBgn + endTag.length();
		ret.innerText = text.substring(ret.bgnEnd, ret.endBgn);

		return ret;
	}

	public static String replace(Enclosed encl, String innerText) {
		return encl.text.substring(0, encl.bgnEnd) + innerText + encl.text.substring(encl.endBgn);
	}

	public static String replaceEnclosed(String text, String bgnTag, String endTag, String innerText) {
		return replace(getEnclosed(text, bgnTag, endTag), innerText);
	}

	public static class Enclosed {
		public String text;
		public int bgnBgn;
		public int bgnEnd;
		public int endBgn;
		public int endEnd;
		public String innerText;
	}

	public static String charsetFilter(String str, String charset) {
		try {
			return new String(str.getBytes(charset), charset);
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw null;
		}
	}

	public static int indexOfChar(String str, String chrs) {
		for(int index = 0; index < str.length(); index++) {
			if(contains(chrs, str.charAt(index))) {
				return index;
			}
		}
		return -1; // not found
	}

	public static int indexOfCharIgnoreCase(String str, String chrs) {
		return indexOfChar(str.toLowerCase(), chrs.toLowerCase());
	}

	public static final String S_ESCAPE = "\u001b";

	public static String reverse(String str) {
		StringBuffer buff = new StringBuffer();

		for(int index = str.length() - 1; 0 <= index; index--) {
			buff.append(str.charAt(index));
		}
		return buff.toString();
	}

	public static String trim(String str) {
		return trim(str, CONTROLCODE + "　");
	}

	public static String trim(String str, String spcChrs) {
		int l;
		int r;

		for(l = 0; l < str.length(); l++) {
			if(contains(spcChrs, str.charAt(l)) == false) {
				break;
			}
		}
		for(r = str.length(); l < r; r--) {
			if(contains(spcChrs, str.charAt(r - 1)) == false) {
				break;
			}
		}
		return str.substring(l, r);
	}

	public static int getCount(String str, char target) {
		int count = 0;

		for(char chr : str.toCharArray()) {
			if(chr == target) {
				count++;
			}
		}
		return count;
	}
}
