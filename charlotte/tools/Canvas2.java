package charlotte.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

public class Canvas2 {
	private Graphics _g;

	public Canvas2(Graphics g) {
		_g = g;
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		_g.setColor(color);
		_g.drawLine(x1, y1, x2, y2);
	}

	public void drawCircle(double x, double y, double r, Color color) {
		drawOval(x, y, r, r, color);
		/*
		final int DENOM = Math.max(40, DoubleTools.toInt(r / 5.0));

		for(int numer = 0; numer < DENOM; numer++) {
			double r1 = Math.PI * 2.0 * numer / DENOM;
			double r2 = Math.PI * 2.0 * (numer + 1) / DENOM;
			double x1 = x + Math.cos(r1) * r;
			double y1 = y + Math.sin(r1) * r;
			double x2 = x + Math.cos(r2) * r;
			double y2 = y + Math.sin(r2) * r;

			drawLine(DoubleTools.toInt(x1),
					DoubleTools.toInt(y1),
					DoubleTools.toInt(x2),
					DoubleTools.toInt(y2),
					color
					);
		}
		*/
	}

	public void fillCircle(double x, double y, double r, Color color) {
		fillOval(x, y, r, r, color);
	}

	public void drawOval(double x, double y, double rX, double rY, Color color) {
		final int DENOM = Math.max(40, DoubleTools.toInt(Math.max(rX, rY) / 5.0));

		for(int numer = 0; numer < DENOM; numer++) {
			double r1 = Math.PI * 2.0 * numer / DENOM;
			double r2 = Math.PI * 2.0 * (numer + 1) / DENOM;
			double x1 = x + Math.cos(r1) * rX;
			double y1 = y + Math.sin(r1) * rY;
			double x2 = x + Math.cos(r2) * rX;
			double y2 = y + Math.sin(r2) * rY;

			drawLine(DoubleTools.toInt(x1),
					DoubleTools.toInt(y1),
					DoubleTools.toInt(x2),
					DoubleTools.toInt(y2),
					color
					);
		}
	}

	public void fillOval(double x, double y, double rX, double rY, Color color) {
		fillOval(
				DoubleTools.toInt(x),
				DoubleTools.toInt(y),
				DoubleTools.toInt(rX),
				DoubleTools.toInt(rY)
				);
	}

	private void fillOval(int x, int y, int rX, int rY) {
		_g.fillOval(
				x - rX,
				y - rY,
				rX * 2,
				rY * 2
				);
	}

	public void drawRect(int l, int t, int w, int h, Color color) {
		drawLine(l, t, l + w, t, color);
		drawLine(l, t, l, t + h, color);
		drawLine(l + w, t, l + w, t + h, color);
		drawLine(l, t + h, l + w, t + h, color);
	}

	public void fillRect(int l, int t, int w, int h, Color color) {
		_g.setColor(color);
		_g.fillRect(l, t, w, h);
	}

	public void fillRectCenter(int centerX, int centerY, int w, int h, Color color) {
		fillRect(centerX - w / 2, centerY - h / 2, w, h, color);
	}

	public void drawDouble(int l, int t, int span, Color color, String value) {
		for(char chr : value.toCharArray()) {
			l += drawDoubleChar(l, t, span, color, chr) + 1;
		}
	}

	private int drawDoubleChar(int l, int t, int span, Color color, char chr) {
		int dSpan = span * 2;
		int drPtn;

		chr = Character.toUpperCase(chr);

		switch(chr) {
		case '.': drPtn = 0b0000100; break;
		case '-': drPtn = 0b0001000; break;
		case '=': drPtn = 0b0001100; break;
		case '0': drPtn = 0b1110111; break;
		case '1': drPtn = 0b0000011; break;
		case '2': drPtn = 0b0111110; break;
		case '3': drPtn = 0b0011111; break;
		case '4': drPtn = 0b1001011; break;
		case '5': drPtn = 0b1011101; break;
		case '6': drPtn = 0b1111101; break;
		case '7': drPtn = 0b1010011; break;
		case '8': drPtn = 0b1111111; break;
		case '9': drPtn = 0b1011111; break;
		case 'A': drPtn = 0b1111011; break;
		case 'B': drPtn = 0b1101101; break;
		case 'C': drPtn = 0b1110100; break;
		case 'D': drPtn = 0b0101111; break;
		case 'E': drPtn = 0b1111100; break;
		case 'F': drPtn = 0b1111000; break;
		case 'G': drPtn = 0b1110101; break;
		case 'H': drPtn = 0b1101001; break;
		case 'I': drPtn = 0b0110100; break;
		case 'J': drPtn = 0b0100111; break;
		case 'K': drPtn = 0b1111001; break;
		case 'L': drPtn = 0b1100100; break;
		case 'M': drPtn = 0b0111001; break;
		case 'N': drPtn = 0b0101001; break;
		case 'O': drPtn = 0b0101101; break;
		case 'P': drPtn = 0b1111010; break;
		case 'Q': drPtn = 0b1011011; break;
		case 'R': drPtn = 0b0101000; break;
		case 'S': drPtn = 0b1001101; break;
		case 'T': drPtn = 0b1101100; break;
		case 'U': drPtn = 0b0100101; break;
		case 'V': drPtn = 0b1100111; break;
		case 'W': drPtn = 0b0110101; break;
		case 'X': drPtn = 0b1101011; break;
		case 'Y': drPtn = 0b1001111; break;
		case 'Z': drPtn = 0b0110110; break;

		default:
			drPtn = 0b0111010;
			break;
		}
		if((drPtn & 0b1000000) != 0) {
			drawLine(l, t, l, t + span, color);
		}
		if((drPtn & 0b100000) != 0) {
			drawLine(l, t + span, l, t + dSpan, color);
		}
		if((drPtn & 0b10000) != 0) {
			drawLine(l, t, l + span, t, color);
		}
		if((drPtn & 0b1000) != 0) {
			drawLine(l, t + span, l + span, t + span, color);
		}
		if((drPtn & 0b100) != 0) {
			drawLine(l, t + dSpan, l + span, t + dSpan, color);
		}
		if((drPtn & 0b10) != 0) {
			drawLine(l + span, t, l + span, t + span, color);
		}
		if((drPtn & 0b1) != 0) {
			drawLine(l + span, t + span, l + span, t + dSpan, color);
		}
		return span + 1;
	}

	public void fillPolygon(XYPoint[] points, Color color) {
		fillPolygon(Arrays.asList(points), color);
	}

	public void fillPolygon(List<XYPoint> points, Color color) {
		int size = points.size();
		int[] xs = new int[size];
		int[] ys = new int[size];

		for(int index = 0; index < size; index++) {
			xs[index] = DoubleTools.toInt(points.get(index).getX());
			ys[index] = DoubleTools.toInt(points.get(index).getY());
		}
		_g.setColor(color);
		_g.fillPolygon(xs, ys, size);
	}
}
