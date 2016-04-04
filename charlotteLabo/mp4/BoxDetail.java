package charlotteLabo.mp4;

import charlotte.tools.ByteReader;

public abstract class BoxDetail {
	public void setBox(Box box) {
		load(new ByteReader(box.image));
	}

	public abstract void load(ByteReader reader);
}
