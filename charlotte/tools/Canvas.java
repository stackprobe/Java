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

	public void drawDouble(int l, int t, int hSpan, Color color, String value) {
		int span = hSpan * 2;

		// TODO
	}
}
