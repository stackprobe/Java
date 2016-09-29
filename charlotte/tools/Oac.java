package charlotte.tools;

public interface Oac<R, P> {
	public void open();
	public R action(P prm);
	public void close();
}
