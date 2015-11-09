package charlotte.tools;

import java.util.Comparator;
import java.util.List;

public class DoubleTools {
	public static final double IMAX = IntTools.IMAX;
	public static final double MICRO = 1.0 / IMAX;

	public static int toInt(double value) {
		return IntTools.toInt(value);
	}

	public static boolean contains(List<Double> list, double target, double margin) {
		for(double value : list) {
			if(equals(value, target, margin)) {
				return true;
			}
		}
		return false;
	}

	public static boolean equals(double a, double b, double margin) {
		return Math.abs(a - b) <= margin;
	}

	public static double[] toArray(List<Double> list) {
		double[] arr = new double[list.size()];

		for(int index = 0; index < list.size(); index++) {
			arr[index] = list.get(index);
		}
		return arr;
	}

	public static Comparator<Double> comp = new Comparator<Double>() {
		@Override
		public int compare(Double a, Double b) {
			double v1 = a.doubleValue();
			double v2 = b.doubleValue();

			if(v1 < v2) {
				return -1;
			}
			if(v2 < v1) {
				return 1;
			}
			return 0;
		}
	};
}
