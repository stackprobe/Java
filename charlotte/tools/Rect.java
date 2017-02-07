package charlotte.tools;

/**
 * immutable
 *
 */
public class Rect {
	private double _l;
	private double _t;
	private double _w; // 0.0 <=
	private double _h; // 0.0 <=

	public Rect() {
		this(0.0, 0.0, 0.0, 0.0);
	}

	public Rect(double w, double h) {
		this(0.0, 0.0, w, h);
	}

	public Rect(Rect rect) {
		this(rect._l, rect._t, rect._w, rect._h);
	}

	public Rect(double l, double t, double w, double h) {
		if(w < 0.0) {
			throw new IllegalArgumentException();
		}
		if(h < 0.0) {
			throw new IllegalArgumentException();
		}
		_l = l;
		_t = t;
		_w = w;
		_h = h;
	}

	public static Rect ltrb(double l, double t, double r, double b) {
		return new Rect(l, t, r - l, b - t);
	}

	public double getL() {
		return _l;
	}

	public double getT() {
		return _t;
	}

	public double getW() {
		return _w;
	}

	public double getH() {
		return _h;
	}

	public double getR() {
		return _l + _w;
	}

	public double getB() {
		return _t + _h;
	}

	public Rect changeL(double l) {
		return new Rect(l, _t, _w, _h);
	}

	public Rect changeT(double t) {
		return new Rect(_l, t, _w, _h);
	}

	public Rect changeW(double w) {
		return new Rect(_l, _t, w, _h);
	}

	public Rect changeH(double h) {
		return new Rect(_l, _t, _w, h);
	}

	public Rect changeR(double r) {
		return ltrb(_l, _t, r, getB());
	}

	public Rect changeB(double b) {
		return ltrb(_l, _t, getR(), b);
	}

	public Rect adjustInside(Rect screen) {
		double new_w = screen._w;
		double new_h = (_h * screen._w) / _w;

		if (screen._h < new_h) {
			new_w = (_w * screen._h) / _h;
			new_h = screen._h;

			if (screen._w < new_w)
				throw null;
		}
		double new_l = screen._l + (screen._w - new_w) / 2.0;
		double new_t = screen._t + (screen._h - new_h) / 2.0;

		return new Rect(new_l, new_t, new_w, new_h);
	}

	public Rect adjustOutside(Rect screen) {
		double new_w = screen._w;
		double new_h = (_h * screen._w) / _w;

		if (new_h < screen._h) {
			new_w = (_w * screen._h) / _h;
			new_h = screen._h;

			if (new_w < screen._w)
				throw null;
		}
		double new_l = screen._l + (screen._w - new_w) / 2.0;
		double new_t = screen._t + (screen._h - new_h) / 2.0;

		return new Rect(new_l, new_t, new_w, new_h);
	}
}
