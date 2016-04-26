package charlotte.tools;

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

	public static XYPoint inverse(XYPoint p) {
		return mul(p, -1.0);
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

	public static XYPoint rotate(XYPoint p, double angle) {
		return rotate(p, angle, getDistance(p));
	}

	public static XYPoint rotate(XYPoint p, double angle, double rNew, XYPoint origin) {
		p = reduce(p, origin);
		p = rotate(p, angle, rNew);
		return add(p, origin);
	}

	public static XYPoint rotate(XYPoint p, double angle, XYPoint origin) {
		p = reduce(p, origin);
		p = rotate(p, angle);
		return add(p, origin);
	}

	public static XYPoint rotateRa(XYPoint p, int ra) {
		ra %= 4;
		ra += 4;
		ra %= 4;

		switch(ra) {
		case 0: return new XYPoint(p.x, p.y);
		case 1: return new XYPoint(-p.y, p.x);
		case 2: return new XYPoint(-p.x, -p.y);
		case 3: return new XYPoint(p.y, -p.x);

		default:
			throw null;
		}
	}
}
