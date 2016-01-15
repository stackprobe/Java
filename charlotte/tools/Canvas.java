package charlotte.tools;

import java.awt.Color;

public class Canvas {
	private Bmp _bmp;

	public Canvas(Bmp bmp) {
		_bmp = bmp;
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		drawLine(x1, y1, x2, y2, new Bmp.Dot(color));
	}

	public void drawLine(int x1, int y1, int x2, int y2, Bmp.Dot dot) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);
		int denom = Math.max(adx, ady);
		denom = Math.max(1, denom);

		for(int numer = 0; numer <= denom; numer++) {
			int x = x1 + DoubleTools.toInt((double)dx * numer / denom);
			int y = y1 + DoubleTools.toInt((double)dy * numer / denom);

			_bmp.setDot(x, y, dot);
		}
	}

	public void drawCircle(double x, double y, double r, Color color) {
		drawCircle(x, y, r, new Bmp.Dot(color));
	}

	public void drawCircle(double x, double y, double r, Bmp.Dot dot) {
		final int DENOM = Math.max(40, DoubleTools.toInt(r / 5.0));

		for(int numer = 0; numer < DENOM; numer++) {
			double r1 = Math.PI * 2 * numer / DENOM;
			double r2 = Math.PI * 2 * (numer + 1) / DENOM;
			double x1 = x + Math.cos(r1) * r;
			double y1 = y + Math.sin(r1) * r;
			double x2 = x + Math.cos(r2) * r;
			double y2 = y + Math.sin(r2) * r;

			drawLine(DoubleTools.toInt(x1),
					DoubleTools.toInt(y1),
					DoubleTools.toInt(x2),
					DoubleTools.toInt(y2),
					dot
					);
		}
	}

	public void drawRect(int l, int t, int w, int h, Color color) {
		drawRect(l, t, w, h, new Bmp.Dot(color));
	}

	public void drawRect(int l, int t, int w, int h, Bmp.Dot dot) {
		for(int x = 0; x < w; x++) {
			_bmp.setDot(
					l + x,
					t,
					dot
					);
			_bmp.setDot(
					l + x,
					t + h - 1,
					dot
					);
		}
		for(int y = 0; y < h; y++) {
			_bmp.setDot(
					l,
					t + y,
					dot
					);
			_bmp.setDot(
					l + w - 1,
					t + y,
					dot
					);
		}
	}

	public void fillRectCenter(int centerX, int centerY, int w, int h, Color color) {
		fillRect(centerX - w / 2, centerY - h / 2, w, h, color);
	}

	public void fillRect(int l, int t, int w, int h, Color color) {
		fillRect(l, t, w, h, new Bmp.Dot(color));
	}

	public void fillRect(int l, int t, int w, int h, Bmp.Dot dot) {
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				_bmp.setDot(l + x, t + y, dot);
			}
		}
	}

	public void drawDouble(int l, int t, int hSpan, Color color, String value) {
		drawDouble(l, t, hSpan, new Bmp.Dot(color), value);
	}

	public void drawDouble(int l, int t, int hSpan, Bmp.Dot dot, String value) {
		for(char chr : value.toCharArray()) {
			l += drawDoubleChar(l, t, hSpan, dot, chr) + 1;
		}
	}

	private int drawDoubleChar(int l, int t, int hSpan, Bmp.Dot dot, char chr) {
		int span = hSpan * 2;
		int dSpan = span * 2;

		if(chr == '.') {
			_bmp.setDot(l, t + dSpan, dot);
			return 1;
		}
		if(chr == '1') {
			drawLine(l, t, l + hSpan, t, dot);
			drawLine(l + hSpan, t, l + hSpan, t + dSpan, dot);
			drawLine(l, t + dSpan, l + span, t + dSpan, dot);
			return span + 1;
		}
		String drPtn = null;

		switch(chr) {
		case '-': drPtn = "0001000"; break;
		case '0': drPtn = "1110111"; break;
		case '2': drPtn = "0111110"; break;
		case '3': drPtn = "0011111"; break;
		case '4': drPtn = "1001011"; break;
		case '5': drPtn = "1011101"; break;
		case '6': drPtn = "1111101"; break;
		case '7': drPtn = "1010011"; break;
		case '8': drPtn = "1111111"; break;
		case '9': drPtn = "1011111"; break;
		default:
			throw new RuntimeException("Unknown chr: " + chr);
		}
		if(drPtn.charAt(0) == '1') {
			drawLine(l, t, l, t + span, dot);
		}
		if(drPtn.charAt(1) == '1') {
			drawLine(l, t + span, l, t + dSpan, dot);
		}
		if(drPtn.charAt(2) == '1') {
			drawLine(l, t, l + span, t, dot);
		}
		if(drPtn.charAt(3) == '1') {
			drawLine(l, t + span, l + span, t + span, dot);
		}
		if(drPtn.charAt(4) == '1') {
			drawLine(l, t + dSpan, l + span, t + dSpan, dot);
		}
		if(drPtn.charAt(5) == '1') {
			drawLine(l + span, t, l + span, t + span, dot);
		}
		if(drPtn.charAt(6) == '1') {
			drawLine(l + span, t + span, l + span, t + dSpan, dot);
		}
		return span + 1;
	}

	public void paste(int x, int y, Color color) {
		paste(x, y, new Bmp.Dot(color));
	}

	public void paste(int x, int y, Bmp.Dot dot) {
		Bmp.Dot tDot = _bmp.getDot(x, y);

		if(tDot.equals(dot)) {
			return;
		}
		Paste p = new Paste(tDot, dot);
		p.bloom(x, y);
		p.burst();
	}

	private class Paste {
		private Bmp.Dot _tDot;
		private Bmp.Dot _dDot;
		private QueueData<int[]> seeds = new QueueData<int[]>();

		public Paste(Bmp.Dot tDot, Bmp.Dot dDot) {
			_tDot = tDot;
			_dDot = dDot;
		}

		public void bloom(int x, int y) {
			if(x < 0) {
				return;
			}
			if(y < 0) {
				return;
			}
			if(_bmp.getWidth() <= x) {
				return;
			}
			if(_bmp.getHeight() <= y) {
				return;
			}
			if(_tDot.equals(_bmp.getDot(x, y)) == false) {
				return;
			}
			_bmp.setDot(x, y, _dDot);

			planting(x - 1, y);
			planting(x + 1, y);
			planting(x, y - 1);
			planting(x, y + 1);
		}

		private void planting(int x, int y) {
			seeds.add(new int[] { x, y });
		}

		public void burst() {
			for(; ; ) {
				int[] seed = seeds.poll();

				if(seed == null) {
					break;
				}
				bloom(seed[0], seed[1]);
			}
		}
	}
}
