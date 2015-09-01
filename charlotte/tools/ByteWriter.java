package charlotte.tools;

public interface ByteWriter {
	public void add(byte chr);
	public void add(byte[] block);
	public void add(byte[] block, int startPos);
	public void add(byte[] block, int startPos, int size);
}
