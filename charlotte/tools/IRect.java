package charlotte.tools;

public class IRect {
	public int l;
	public int t;
	public int w;
	public int h;

	public IRect() {
		this(0, 0, 0, 0);
	}

	public IRect(IRect rect) {
		this(rect.l, rect.t, rect.w, rect.h);
	}

	public IRect(int l, int t, int w, int h) {
		this.l = l;
		this.t = t;
		this.w = w;
		this.h = h;
	}

	public static IRect ltrb(int l, int t, int r, int b) {
		return new IRect(l, t, r - l, b - t);
	}

	public int getR() {
		return l + w;
	}

	public int getB() {
		return t + h;
	}

	public void setR(int r) {
		w = r - l;
	}

	public void setB(int b) {
		h = b - t;
	}

	public IRect extend(int l, int t, int r, int b) {
		IRect ret = new IRect(this);

		ret.l -= l;
		ret.t -= t;
		ret.w += l + r;
		ret.h += t + b;

		return ret;
	}

	public IRect unextend(int l, int t, int r, int b) {
		IRect ret = new IRect(this);

		ret.l += l;
		ret.t += t;
		ret.w -= l + r;
		ret.h -= t + b;

		return ret;
	}

	public IRect getClone() {
		return new IRect(this);
	}
}
