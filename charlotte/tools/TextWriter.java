package charlotte.tools;

public abstract class TextWriter {
	public abstract void open();
	public abstract void close();
	public abstract void writeLine(String line);

	public void writeError(String line) {
		writeLine(line);
	}
}
