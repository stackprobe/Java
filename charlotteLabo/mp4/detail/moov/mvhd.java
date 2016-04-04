package charlotteLabo.mp4.detail.moov;

import charlotte.tools.ByteReader;
import charlotteLabo.mp4.BoxDetail;
import charlotteLabo.mp4.element.EInt;

public class mvhd extends BoxDetail {
	public EInt version = EInt.createLE(4);
	public EInt createdMacUTCDate  = EInt.createLE(4);
	public EInt modifiedMacUTCDate = EInt.createLE(4);
	public EInt timeScale = EInt.createLE(4);
	public EInt duration  = EInt.createLE(4);

	@Override
	public void load(ByteReader reader) {
		version.load(reader);

		if(version.getValue() == 1) {
			createdMacUTCDate.setSize(8);
			modifiedMacUTCDate.setSize(8);
			duration.setSize(8);
		}

		// TODO
	}
}
