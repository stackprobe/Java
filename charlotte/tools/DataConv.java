package charlotte.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DataConv {
	public static List<String> getStringList(int[] src) {
		List<String> dest = new ArrayList<String>();

		for(int val : src) {
			dest.add("" + val);
		}
		return dest;
	}

	public static String toString(Color color) {
		return StringTools.toHex(IntTools.toBytesBE(color.getRGB()));
	}
}
