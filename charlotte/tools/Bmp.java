package charlotte.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Bmp {
	private int[] _list;
	private int _w;
	private int _h;

	public Bmp(int w, int h, Color color) {
		this(w, h, new Dot(color));
	}

	public Bmp(int w, int h, Dot dot) {
		this(w, h, dot.a, dot.r, dot.g, dot.b);
	}

	public Bmp(int w, int h, int a, int r, int g, int b) {
		this(w, h);

		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				for(int col = 0; col < 4; col++) {
					setA(x, y, a);
					setR(x, y, r);
					setG(x, y, g);
					setB(x, y, b);
				}
			}
		}
	}

	public Bmp(int w, int h) {
		_list = new int[w * h];
		_w = w;
		_h = h;
	}

	public int getWidth() {
		return _w;
	}

	public int getHeight() {
		return _h;
	}

	public int get(int x, int y) {
		return _list[x + _w * y];
	}

	public void set(int x, int y, int val) {
		_list[x + _w * y] = val;
	}

	private boolean isFairPoint(int x, int y) {
		return 0 <= x && x < _w && 0 <= y && y < _h;
	}

	public int DUMMY_ARGB = 127;

	public int getC(int x, int y, int col) {
		if(isFairPoint(x, y) == false) {
			return DUMMY_ARGB;
		}
		int index = x + _w * y;
		int bit = col * 8;

		return (_list[index] >>> bit) & 0xff;
	}

	public void setC(int x, int y, int col, int val) {
		if(isFairPoint(x, y) == false) {
			return;
		}
		int index = x + _w * y;
		int bit = col * 8;

		_list[index] &= ~(0xff << bit);
		_list[index] |= (val << bit);
	}

	public static final int A = 0;
	public static final int R = 1;
	public static final int G = 2;
	public static final int B = 3;

	public int getA(int x, int y) {
		return getC(x, y, A);
	}

	public int getR(int x, int y) {
		return getC(x, y, R);
	}

	public int getG(int x, int y) {
		return getC(x, y, G);
	}

	public int getB(int x, int y) {
		return getC(x, y, B);
	}

	public void setA(int x, int y, int val) {
		setC(x, y, A, val);
	}

	public void setR(int x, int y, int val) {
		setC(x, y, R, val);
	}

	public void setG(int x, int y, int val) {
		setC(x, y, G, val);
	}

	public void setB(int x, int y, int val) {
		setC(x, y, B, val);
	}

	public static Bmp getBmp(BufferedImage bi) {
		Bmp bmp = new Bmp(bi.getWidth(), bi.getHeight());

		for(int x = 0; x < bmp.getWidth(); x++) {
			for(int y = 0; y < bmp.getHeight(); y++) {
				int argb = bi.getRGB(x, y);

				int a = (argb >>> 24) & 0xff;
				int r = (argb >>> 16) & 0xff;
				int g = (argb >>>  8) & 0xff;
				int b = (argb >>>  0) & 0xff;

				bmp.setA(x, y, a);
				bmp.setR(x, y, r);
				bmp.setG(x, y, g);
				bmp.setB(x, y, b);
			}
		}
		return bmp;
	}

	public static Bmp getBmp(byte[] imageData) throws Exception {
		return getBmp(ImageIO.read(new ByteArrayInputStream(imageData)));
	}

	public static Bmp fromFile(String file) throws Exception {
		return getBmp(FileTools.readAllBytes(file));
	}

	public static Bmp getBmp(PngData image) throws Exception {
		return getBmp(image.getBytes());
	}

	public void toBi(BufferedImage bi) {
		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				int a = getA(x, y);
				int r = getR(x, y);
				int g = getG(x, y);
				int b = getB(x, y);

				int argb = (a << 24) | (r << 16) | (g << 8) | b;

				bi.setRGB(x, y, argb);
			}
		}
	}

	public BufferedImage getBi() {
		BufferedImage bi = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
		toBi(bi);
		return bi;
	}

	public byte[] getBytes(String format) throws Exception {
		BufferedImage bi = getBi();
		String workFile = FileTools.makeTempPath();
		FileOutputStream os = new FileOutputStream(workFile);

		try {
			ImageIO.write(bi, format, os);
			os.close();
			return FileTools.readAllBytes(workFile);
		}
		finally {
			FileTools.close(os);
			FileTools.delete(workFile);
		}
	}

	public byte[] getBytes() throws Exception {
		return getBytes("png");
	}

	public void writeToFile(String file) throws Exception {
		FileTools.writeAllBytes(file, getBytes());
	}

	public Bmp cut() {
		return cut(0, 0, _w, _h);
	}

	public Bmp cut(int l, int t, int w, int h) {
		Bmp dest = new Bmp(w, h);

		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				dest.set(x, y, get(l + x, t + y));
			}
		}
		return dest;
	}

	public int DUMMY_A = 255;
	public int DUMMY_R = 100;
	public int DUMMY_G = 140;
	public int DUMMY_B = 120;

	public Bmp copy(int l, int t, int w, int h) {
		return copy(l, t, w, h, new Dot(DUMMY_A, DUMMY_R, DUMMY_G, DUMMY_B));
	}

	public Bmp copy(int l, int t, int w, int h, Dot outerDot) {
		Bmp dest = new Bmp(w, h);

		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int sx = l + x;
				int sy = t + y;

				if(0 <= sx && sx < _w && 0 <= sy && sy < _h)
					dest.set(x, y, get(sx, sy));
				else
					dest.setDot(x, y, outerDot);
			}
		}
		return dest;
	}

	public void paste(Bmp surface) {
		paste(
				surface,
				(_w - surface.getWidth()) / 2,
				(_h - surface.getHeight()) / 2
				);
	}

	public void paste(Bmp surface, int l, int t) {
		for(int x = 0; x < surface.getWidth(); x++) {
			for(int y = 0; y < surface.getHeight(); y++) {
				int as = surface.getA(x, y);
				int ag = getA(l + x, t + y);

				ag *= 255 - as;
				ag = (int)LongTools.divRndOff(ag, 255);

				setA(l + x, t + y, as + ag);

				for(int col = 1; col < 4; col++) {
					int vals = surface.getC(x, y, col);
					int valg = getC(l + x, t + y, col);
					int val = vals * as + valg * ag;

					val = (int)LongTools.divRndOff(val, as + ag);

					setC(l + x, t + y, col, val);
				}
			}
		}
	}

	public void simplePaste(Bmp surface, int l, int t) {
		for(int x = 0; x < surface.getWidth(); x++) {
			for(int y = 0; y < surface.getHeight(); y++) {
				setDot(l + x, t + y, surface.getDot(x, y));
			}
		}
	}

	public Bmp extend(int l, int t, int r, int b) {
		return extend(l, t, r, b, new Dot(DUMMY_R, DUMMY_G, DUMMY_B, DUMMY_A));
	}

	public Bmp extend(int l, int t, int r, int b, Dot outerDot) {
		Bmp dest = new Bmp(l + _w + r, t + _h + b, outerDot);
		dest.paste(this, l, t);
		return dest;
	}

	public Bmp expand(int dest_w, int dest_h) {
		Bmp dest = new Bmp(dest_w, dest_h);
		new Expand(this, dest).go();
		return dest;
	}

	public static final int A_0_RGB = 108;
	public static final Dot A_0_DOT = new Dot(0, A_0_RGB, A_0_RGB, A_0_RGB);

	public static class Expand {
		private Bmp _src;
		private int _src_w;
		private int _src_h;
		private Bmp _dest;
		private int _dest_w;
		private int _dest_h;

		public Expand(Bmp src, Bmp dest) {
			_src = src;
			_src_w = src.getWidth();
			_src_h = src.getHeight();
			_dest = dest;
			_dest_w = dest.getWidth();
			_dest_h = dest.getHeight();
		}

		private List<LineInfo> _vLines;
		private List<LineInfo> _hLines;

		private class LineInfo {
			public List<RangeInfo> rangeList = new ArrayList<RangeInfo>();
		}

		private class RangeInfo {
			//public int dPos;
			public int sPos;
			public int eBgn;
			public int eEnd;
			public int count;
		}

		private List<LineInfo> makeLines(int src_w, int dest_w) {
			List<LineInfo> lines = new ArrayList<LineInfo>();

			for(int dPos = 0; dPos < dest_w; dPos++) {
				int eBgn = dPos * src_w;
				int eEnd = (dPos + 1) * src_w - 1;

				int sBgn = eBgn / dest_w;
				int sEnd = eEnd / dest_w;

				LineInfo li = new LineInfo();

				for(int sPos = sBgn; sPos <= sEnd; sPos++) {
					RangeInfo ri = new RangeInfo();

					//ri.dPos = dPos;
					ri.sPos = sPos;

					if(sPos == sBgn)
						ri.eBgn = eBgn;
					else
						ri.eBgn = sPos * dest_w;

					if(sPos == sEnd)
						ri.eEnd = eEnd;
					else
						ri.eEnd = (sPos + 1) * dest_w - 1;

					ri.count = ri.eEnd + 1 - ri.eBgn;

					li.rangeList.add(ri);
				}
				lines.add(li);
			}
			return lines;
		}

		public void go() {
			_vLines = makeLines(_src_w, _dest_w);
			_hLines = makeLines(_src_h, _dest_h);

			for(int x = 0; x < _dest_w; x++) {
				LineInfo vli = _vLines.get(x);

				for(int y = 0; y < _dest_h; y++) {
					LineInfo hli = _hLines.get(y);

					for(int col = 0; col < 4; col++) {
						long numer = 0;
						long denom = 0;

						for(int v = 0; v < vli.rangeList.size(); v++) {
							RangeInfo vri = vli.rangeList.get(v);

							for(int h = 0; h < hli.rangeList.size(); h++) {
								RangeInfo hri = hli.rangeList.get(h);

								long weight = (long)vri.count * hri.count;

								if(col != A)
									weight *= _src.getA(vri.sPos, hri.sPos);

								numer += weight * _src.getC(vri.sPos, hri.sPos, col);
								denom += weight;
							}
						}
						int val;

						if(denom == 0)
							val = A_0_RGB;
						else
							val = (int)LongTools.divRndOff(numer, denom);

						_dest.setC(x, y, col, val);
					}
				}
			}
		}
	}

	public Bmp rotateLq(double angle) {
		return rotate(angle, 4);
	}

	public Bmp rotate(double angle) {
		return rotate(angle, 16);
	}

	public Bmp rotate(double angle, int div) {
		return rotate(angle, div, _w / 2.0, _h / 2.0, DUMMY_A, DUMMY_R, DUMMY_G, DUMMY_B);
	}

	public Bmp rotate(double angle, int div, double center_x, double center_y, int outer_a, int outer_r, int outer_g, int outer_b) {
		Bmp dest = new Bmp(_w, _h);

		Dot[] dots = new Dot[div * div];
		Dot outerDot = new Dot(outer_a, outer_r, outer_g, outer_b);

		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				int c = 0;

				for(int cx = 0; cx  < div; cx++) {
					for(int cy = 0; cy < div; cy++) {
						double dx = x + (cx + 0.5) / div;
						double dy = y + (cy + 0.5) / div;

						dx -= center_x;
						dy -= center_y;

						{
							double dw;

							dw = dx * Math.cos(angle) - dy * Math.sin(angle);
							dy = dx * Math.sin(angle) + dy * Math.cos(angle);
							dx = dw;
						}

						dx += center_x;
						dy += center_y;

						int ix = (int)dx;
						int iy = (int)dy;

						if(0 <= ix && ix < _w && 0 <= iy && iy < _h)
							dots[c++] = getDot(ix, iy);
						else
							dots[c++] = outerDot;
					}
				}
				dest.setDot(x, y, mix(dots));
			}
		}
		return dest;
	}

	public static class Dot {
		public int a;
		public int r;
		public int g;
		public int b;

		public Dot(int value) {
			this(new Color(value, true));
		}

		public Dot(Color color) {
			this.a = color.getAlpha();
			this.r = color.getRed();
			this.g = color.getGreen();
			this.b = color.getBlue();
		}

		public Dot(int a, int r, int g, int b) {
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;

			if(obj instanceof Dot == false)
				return false;

			Dot dot = (Dot)obj;

			if(a == 0 && dot.a == 0) return true;

			if(a != dot.a) return false;
			if(r != dot.r) return false;
			if(g != dot.g) return false;
			if(b != dot.b) return false;

			return true;
		}

		@Override
		public String toString() {
			return a + ", " + r + ", " + g + ", " + b;
		}

		public Color getColor() {
			return new Color(r, g, b, a);
		}
	}

	public Dot getDot(int x, int y) {
		return new Dot(getA(x, y), getR(x, y), getG(x, y), getB(x, y));
	}

	public void setDot(int x, int y, Dot dot) {
		setA(x, y, dot.a);
		setR(x, y, dot.r);
		setG(x, y, dot.g);
		setB(x, y, dot.b);
	}

	public static Dot mix(Dot[] dots) {
		long a = 0;
		long r = 0;
		long g = 0;
		long b = 0;

		for(Dot dot : dots) {
			a += dot.a;
			r += dot.r * dot.a;
			g += dot.g * dot.a;
			b += dot.b * dot.a;
		}
		if(a == 0) {
			return A_0_DOT;
		}

		int ia = (int)LongTools.divRndOff(a, dots.length);
		int ir = (int)LongTools.divRndOff(r, a);
		int ig = (int)LongTools.divRndOff(g, a);
		int ib = (int)LongTools.divRndOff(b, a);

		return new Dot(ia, ir, ig, ib);
	}

	public Bmp trim(Dot outerDot) {
		return trim(outerDot, 0);
	}

	public Bmp trim(Dot outerDot, int margin) {
		int l = _w - 1;
		int t = _h - 1;
		int r = 0;
		int b = 0;

		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				if(getDot(x, y).equals(outerDot) == false) {
					l = Math.min(l, x);
					t = Math.min(t, y);
					r = Math.max(r, x);
					b = Math.max(b, y);
				}
			}
		}
		l -= margin;
		t -= margin;
		r += margin;
		b += margin;

		trimCheckLW("TRIM_L: ", l, _w);
		trimCheckLW("TRIM_T: ", t, _h);
		trimCheckLW("TRIM_R: ", (_w - r), _w);
		trimCheckLW("TRIM_B: ", (_h - b), _h);

		int w = r - l + 1;
		int h = b - t + 1;

		Bmp bmp = copy(l, t, w, h, outerDot);

		bmp.trimmed_l = l;
		bmp.trimmed_t = t;
		bmp.trimmed_r = r;
		bmp.trimmed_b = b;

		return bmp;
	}

	private static boolean _trimCheckLW_Alert;

	private void trimCheckLW(String title, int l, int w) {
		double rate = (double)l / w;
		if(rate < 0.1) {
			_trimCheckLW_Alert = true;
		}
		(_trimCheckLW_Alert ? System.err : System.out).println(title + l + " (" + rate + ") " + w);
	}

	public int trimmed_l = -1;
	public int trimmed_t = -1;
	public int trimmed_r = -1;
	public int trimmed_b = -1;

	public void swap(int x1, int y1, int x2, int y2) {
		int tmp = get(x1, y1);
		set(x1, y1, get(x2, y2));
		set(x2, y2, tmp);
	}

	/**
	 * (0, 0)-(1, 1)を軸に反転
	 * @return
	 */
	public Bmp twist() {
		Bmp dest = new Bmp(_h, _w);

		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				dest.set(y, x, get(x, y));
			}
		}
		return dest;
	}

	/**
	 * 左右反転
	 */
	public void mirror() {
		for(int x = 0; x < _w / 2; x++) {
			for(int y = 0; y < _h; y++) {
				swap(x, y, _w - 1 - x, y);
			}
		}
	}

	/**
	 * 上下反転
	 */
	public void reverse() {
		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h / 2; y++) {
				swap(x, y, x, _h - 1 - y);
			}
		}
	}

	/**
	 * 時計回り_90°(1直角)回転
	 * @return
	 */
	public Bmp rotate_ra1() {
		Bmp dest = twist();
		dest.mirror();
		return dest;
	}

	/**
	 * 時計回り_180(2直角)回転°
	 */
	public void rotate_ra2() {
		mirror();
		reverse();
	}

	/**
	 * 時計回り_270°(3直角)回転
	 */
	public Bmp rotate_ra3() {
		Bmp dest = twist();
		dest.reverse();
		return dest;
	}

	/**
	 * 反時計回り_90°(1直角)回転
	 * @return
	 */
	public Bmp rotate_ccw_ra1() {
		return rotate_ra3();
	}

	/**
	 * 反時計回り_180(2直角)回転°
	 */
	public void rotate_ccw_ra2() {
		rotate_ra2();
	}

	/**
	 * 反時計回り_270°(3直角)回転
	 */
	public Bmp rotate_ccw_ra3() {
		return rotate_ra1();
	}

	public static BufferedImage getImage(byte[] imageData) throws Exception {
		return Bmp.getBmp(imageData).getBi();
	}

	public static BufferedImage getImage(PngData image) throws Exception {
		return getImage(image.getBytes());
	}
}
