package charlotte.tools;

public class DoubleTools {
	public static double toInt(double value) {
		if(value < 0.0) {
			return (int)(value - 0.5);
		}
		return (int)(value + 0.5);
	}
}
