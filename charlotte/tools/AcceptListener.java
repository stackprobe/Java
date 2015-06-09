package charlotte.tools;

public interface AcceptListener<T> {
	public boolean accept(T source) throws Exception;
}
