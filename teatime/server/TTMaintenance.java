package teatime.server;

public interface TTMaintenance {
	public long nextPerformTime() throws Exception;
	public void perform() throws Exception;
}
