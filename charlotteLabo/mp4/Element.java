package charlotteLabo.mp4;

import charlotte.tools.ByteReader;

public abstract class Element {
	public abstract void load(ByteReader reader);
	public abstract String getString();
}
