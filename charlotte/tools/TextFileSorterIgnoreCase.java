package charlotte.tools;

public class TextFileSorterIgnoreCase extends TextFileSorter {
	public TextFileSorterIgnoreCase(String charset) {
		super(charset);
	}

	@Override
	protected int comp(String a, String b) {
		return StringTools.compIgnoreCase.compare(a, b);
	}
}
