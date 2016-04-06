package charlotteLabo.mp4.detail;

import charlotteLabo.mp4.BoxDetail;

public class stts extends BoxDetail {
	public int version;
	public int bytesNumberOfTimes;
	public int bytesTimePerFrame_a;
	public int bytesTimePerFrame_b;

	@Override
	protected void load() {
		version = readIntLE(4);
		bytesNumberOfTimes = readIntBE(4);
		bytesTimePerFrame_a = readIntBE(4);
		bytesTimePerFrame_b = readIntBE(4);
	}
}
