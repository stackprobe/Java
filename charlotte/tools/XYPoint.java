package charlotte.tools;

import java.util.Arrays;
import java.util.List;

public class XYPoint {
	public double x;
	public double y;

	public XYPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static XYPoint add(XYPoint a, XYPoint b) {
		return new XYPoint(
				a.x + b.x,
				a.y + b.y
				);
	}

	public static XYPoint reduce(XYPoint a, XYPoint b) {
		return new XYPoint(
				a.x - b.x,
				a.y - b.y
				);
	}

	public static XYPoint mul(XYPoint a, double r) {
		return new XYPoint(
				a.x * r,
				a.y * r
				);
	}

	public static XYPoint mul(XYPoint p, double r, XYPoint origin) {
		p = reduce(p, origin);
		p = mul(p, r);
		return add(p, origin);
	}

	public static XYPoint opposite(XYPoint p) {
		return mul(p, -1.0);
	}

	public static XYPoint reciprocal(XYPoint p) {
		return new XYPoint(
				1.0 / p.x,
				1.0 / p.y
				);
	}

	public static XYPoint div(XYPoint a, double r) {
		return new XYPoint(
				a.x / r,
				a.y / r
				);
	}

	public static XYPoint div(XYPoint p, double r, XYPoint origin) {
		p = reduce(p, origin);
		p = div(p, r);
		return add(p, origin);
	}

	public static XYPoint getPoint(double angle) {
		return new XYPoint(
				Math.cos(angle),
				Math.sin(angle)
				);
	}

	public static XYPoint getPoint(double angle, double r) {
		return mul(getPoint(angle), r);
	}

	public static XYPoint getPoint(double angle, double r, XYPoint origin) {
		return add(getPoint(angle, r), origin);
	}

	public static double getAngle(XYPoint p) {
		if(p.y < 0.0) {
			return Math.PI * 2.0 - getAngle(new XYPoint(p.x, -p.y));
		}
		if(p.x < 0.0) {
			return Math.PI - getAngle(new XYPoint(-p.x, p.y));
		}
		if(p.y == 0.0) {
			return 0.0;
		}
		if(p.x == 0.0) {
			return Math.PI / 2.0;
		}
		double h = Math.PI / 2.0;
		double l = 0.0;
		double t = p.y / p.x;

		for(int c = 0; c < 50; c++) {
			double m = (h + l) / 2.0;
			double tt = Math.tan(m);

			if(tt < t) {
				l = m;
			}
			else {
				h = m;
			}
		}
		//System.out.println("d: " + (h - l)); // test
		return (h + l) / 2.0;
	}

	public static double getAngle(XYPoint p, XYPoint origin) {
		return getAngle(reduce(p, origin));
	}

	public static double getDistance(XYPoint p) {
		return Math.sqrt(p.x * p.x + p.y * p.y);
	}

	public static double getDistance(XYPoint a, XYPoint b) {
		return getDistance(reduce(a, b));
	}

	public static XYPoint rotate(XYPoint p, double angle, double rNew) {
		return getPoint(getAngle(p) + angle, rNew);
	}

	public static XYPoint rotate(XYPoint p, double angle, double rNew, XYPoint origin) {
		p = reduce(p, origin);
		p = rotate(p, angle, rNew);
		return add(p, origin);
	}

	public static XYPoint rotate(XYPoint p, double angle) {
		return rotate(p, angle, getDistance(p));
	}

	public static XYPoint rotate(XYPoint p, double angle, XYPoint origin) {
		p = reduce(p, origin);
		p = rotate(p, angle);
		return add(p, origin);
	}

	public static XYPoint rotd(XYPoint p, int degree) {
		degree %= 360;
		degree += 360;
		degree %= 360;

		switch(degree) {
		case 0: return new XYPoint(p.x, p.y);
		case 90: return new XYPoint(-p.y, p.x);
		case 180: return new XYPoint(-p.x, -p.y);
		case 270: return new XYPoint(p.y, -p.x);
		}
		return rotate(p, ((double)degree * Math.PI) / 180.0);
	}

	public static XYPoint rotd(XYPoint p, int degree, XYPoint origin) {
		p = reduce(p, origin);
		p = rotd(p, degree);
		return add(p, origin);
	}

	public static XYPoint average(List<XYPoint> list) {
		double x = list.get(0).x;
		double y = list.get(0).y;

		for(int index = 1; index < list.size(); index++) {
			x += list.get(index).x;
			y += list.get(index).y;
		}
		return new XYPoint(
				x / list.size(),
				y / list.size()
				);
	}

	public static XYPoint average(XYPoint[] arr) {
		return average(Arrays.asList(arr));
	}

	public static final XYPoint ORIGIN = new XYPoint(0.0, 0.0);

	public static double getNearestDistance(List<XYPoint> list) {
		return getNearestDistance(list, ORIGIN);
	}

	public static double getNearestDistance(List<XYPoint> list, XYPoint origin) {
		return getDistance(getNearest(list, origin), origin);
	}

	public static XYPoint getNearest(List<XYPoint> list) {
		return getNearest(list, ORIGIN);
	}

	public static XYPoint getNearest(List<XYPoint> list, XYPoint origin) {
		return list.get(getNearestIndex(list, origin));
	}

	public static int getNearestIndex(List<XYPoint> list) {
		return getNearestIndex(list, ORIGIN);
	}

	public static int getNearestIndex(List<XYPoint> list, XYPoint origin) {
		int ret = 0;
		double nearestD = getDistance(list.get(0), origin);

		for(int index = 1; index < list.size(); index++) {
			double d = getDistance(list.get(index), origin);

			if(d < nearestD) {
				ret = index;
				nearestD = d;
			}
		}
		return ret;
	}
}
