package charlotte.tools;

public interface TextWriter {
	public void open();
	public void writeLine(String line);
	public void close();
}
