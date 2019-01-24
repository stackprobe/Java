package charlotte.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.goui.util.MTRandom;

public class MathTools {
	private static Random _random = new MTRandom();

	public static int random(int modulo) {
		return _random.nextInt(modulo);
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

	public static char random(String src) {
		return src.charAt(random(src.length()));
	}

	public static Color getRandColor() {
		return new Color(
				random(256),
				random(256),
				random(256)
				);
	}

	public static <T> List<T> randomSubList(List<T> src) {
		return randomSubList(src, MathTools.random(0, src.size()));
	}

	public static <T> List<T> randomSubList(List<T> src, int count) {
		List<T> dest = new ArrayList<T>();

		src = ArrayTools.copy(src);

		while(dest.size() < count) {
			int index = random(src.size());

			dest.add(src.get(index));
			ArrayTools.fastRemove(src, index);
		}
		return dest;
	}

	public static int root(long value) {
		if(value < 0) {
			throw new IllegalArgumentException();
		}
		int ret = 0;

		for(int bit = 1 << 31; bit != 0; bit >>= 1) {
			int t = ret | bit;

			if((long)t * t <= value) {
				ret = t;
			}
		}
		return ret;
	}
}
