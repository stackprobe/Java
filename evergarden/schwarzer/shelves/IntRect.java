package evergarden.schwarzer.shelves;

public class IntRect {
	public int l;
	public int t;
	public int w;
	public int h;

	public IntRect() {
		this(0, 0, 0, 0);
	}

	public IntRect(IntRect rect) {
		this(rect.l, rect.t, rect.w, rect.h);
	}

	public IntRect(int l, int t, int w, int h) {
		this.l = l;
		this.t = t;
		this.w = w;
		this.h = h;
	}

	public static IntRect ltrb(int l, int t, int r, int b) {
		return new IntRect(l, t, r - l, b - t);
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

	public IntRect extend(int l, int t, int r, int b) {
		IntRect ret = new IntRect(this);

		ret.l -= l;
		ret.t -= t;
		ret.w += l + r;
		ret.h += t + b;

		return ret;
	}

	public IntRect unextend(int l, int t, int r, int b) {
		IntRect ret = new IntRect(this);

		ret.l += l;
		ret.t += t;
		ret.w -= l + r;
		ret.h -= t + b;

		return ret;
	}

	public IntRect getClone() {
		return new IntRect(this);
	}
}
