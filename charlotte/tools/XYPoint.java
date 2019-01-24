package charlotte.tools;

/**
 * immutable
 *
 */
public class XYPoint {
	private double _x;
	private double _y;

	public XYPoint() {
		this(0.0, 0.0);
	}

	public XYPoint(double x, double y) {
		_x = x;
		_y = y;
	}

	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}

	public XYPoint changeX(double x) {
		return new XYPoint(x, _y);
	}

	public XYPoint changeY(double y) {
		return new XYPoint(_x, y);
	}

	public XYPoint add(XYPoint p) {
		return new XYPoint(
				_x + p._x,
				_y + p._y
				);
	}

	public XYPoint reduce(XYPoint p) {
		return new XYPoint(
				_x - p._x,
				_y - p._y
				);
	}

	public static final XYPoint ORIGIN = new XYPoint();

	public XYPoint mul(double r) {
		return new XYPoint(
				_x * r,
				_y * r
				);
	}

	public XYPoint mul(double r, XYPoint origin) {
		return reduce(origin).mul(r);
	}

	public XYPoint div(double r) {
		return new XYPoint(
				_x / r,
				_y / r
				);
	}

	public XYPoint div(double r, XYPoint origin) {
		return reduce(origin).div(r);
	}

	public XYPoint opposite() {
		return mul(-1.0);
	}

	public XYPoint reciprocal(XYPoint p) {
		return new XYPoint(
				1.0 / _x,
				1.0 / _y
				);
	}

	public static XYPoint getPoint(double angle) {
		return new XYPoint(
				Math.cos(angle),
				Math.sin(angle)
				);
	}

	public static XYPoint getPoint(double angle, double r) {
		return getPoint(angle).mul(r);
	}

	public static XYPoint getPoint(double angle, double r, XYPoint origin) {
		return getPoint(angle, r).add(origin);
	}

	public double getAngle() {
		if(_y < 0.0) {
			return Math.PI * 2.0 - changeY(-_y).getAngle();
		}
		if(_x < 0.0) {
			return Math.PI - changeX(-_x).getAngle();
		}
		if(_y == 0.0) {
			return 0.0;
		}
		if(_x == 0.0) {
			return Math.PI / 2.0;
		}
		double h = Math.PI / 2.0;
		double l = 0.0;
		double t = _y / _x;

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
		return (h + l) / 2.0;
	}

	public double getAngle(XYPoint origin) {
		return reduce(origin).getAngle();
	}

	public double getDistance() {
		return Math.sqrt(_x * _x + _y * _y);
	}

	public double getDistance(XYPoint origin) {
		return reduce(origin).getDistance();
	}

	public XYPoint rotate(double angle, double r) {
		return getPoint(getAngle() + angle, r);
	}

	public XYPoint rotate(double angle, double r, XYPoint origin) {
		return reduce(origin).rotate(angle, r).add(origin);
	}

	public XYPoint rotate(double angle) {
		return rotate(angle, getDistance());
	}

	public XYPoint rotate(double angle, XYPoint origin) {
		return reduce(origin).rotate(angle).add(origin);
	}

	public XYPoint rotdeg(int degree) {
		degree %= 360;
		degree += 360;
		degree %= 360;

		switch(degree) {
		case 0: return this;
		case 90: return new XYPoint(-_y, _x);
		case 180: return new XYPoint(-_x, -_y);
		case 270: return new XYPoint(_y, -_x);
		}
		return rotate((double)degree * Math.PI / 180.0);
	}

	public XYPoint rotdeg(int degree, XYPoint origin) {
		return reduce(origin).rotdeg(degree).add(origin);
	}
}
