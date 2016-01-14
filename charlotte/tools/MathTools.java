package charlotte.tools;

import java.awt.Color;
import java.util.List;

public class MathTools {
	public static int random(int modulo) {
		return (int)(Math.random() * modulo);
	}

	public static int random(int minval, int maxval) {
		return random(maxval + 1 - minval) + minval;
	}

	public static <T> T random(List<T> list) {
		return list.get(random(list.size()));
	}

	public static <T> T random(T[] arr) {
		return arr[random(arr.length)];
	}

	public static Color getRandColor() {
		return new Color(
				random(256),
				random(256),
				random(256)
				);
	}
}
