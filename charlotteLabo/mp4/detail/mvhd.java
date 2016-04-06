package charlotteLabo.mp4.detail;

import charlotteLabo.mp4.BoxDetail;

public class mvhd extends BoxDetail {
	public int version;
	public int timeScale;
	public long duration;
	public int rate;
	public int volume;

	@Override
	protected void load() {
		version = readIntLE(4);
		seek(version == 0 ? 4 : 8); // createdDate
		seek(version == 0 ? 4 : 8); // modifiedDate
		timeScale = readIntBE(4);
		duration = readLongBE(version == 0 ? 4 : 8);
		rate = readIntBE(4);
		volume = readIntBE(2);
	}
}
