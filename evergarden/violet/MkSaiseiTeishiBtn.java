package evergarden.violet;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.IntTools;
import charlotte.tools.XYPoint;

public class MkSaiseiTeishiBtn {
	public static void main(String[] args) {
		try {
			new MkSaiseiTeishiBtn().main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private Bmp _bmp;

	private void main2() throws Exception {
		_bmp = new Bmp(1000, 1000);

		clear();
		drawCircle();
		drawSaisei();
		expand();

		_bmp.writeToFile("C:/temp/button_saisei.png");
		_bmp = new Bmp(1000, 1000);

		clear();
		drawCircle();
		drawTeishi();
		expand();

		_bmp.writeToFile("C:/temp/button_teishi.png");
	}

	private void clear() {
		for(int x = 0; x < 1000; x++) {
			for(int y = 0; y < 1000; y++) {
				_bmp.setDot(x, y, new Bmp.Dot(0, 0, 0, 0));
			}
		}
	}

	private void drawCircle() {
		for(int x = 0; x < 1000; x++) {
			for(int y = 0; y < 1000; y++) {
				XYPoint pt = new XYPoint(x, y);
				double d = pt.getDistance(new XYPoint(500, 500));

				if(400.0 < d && d < 450.0) {
					_bmp.setDot(x, y, new Bmp.Dot(255, 255, 255, 255));
				}
			}
		}
	}

	private void drawSaisei() {
		Canvas canvas = new Canvas(_bmp);

		XYPoint pt1 = XYPoint.getPoint(Math.PI * 2.0 / 3.0 * 0.0, 350.0, new XYPoint(500.0, 500.0));
		XYPoint pt2 = XYPoint.getPoint(Math.PI * 2.0 / 3.0 * 1.0, 350.0, new XYPoint(500.0, 500.0));
		XYPoint pt3 = XYPoint.getPoint(Math.PI * 2.0 / 3.0 * 2.0, 350.0, new XYPoint(500.0, 500.0));

		canvas.drawLine(
				IntTools.toInt(pt1.getX()),
				IntTools.toInt(pt1.getY()),
				IntTools.toInt(pt2.getX()),
				IntTools.toInt(pt2.getY()),
				new Bmp.Dot(255, 255, 255, 255)
				);
		canvas.drawLine(
				IntTools.toInt(pt2.getX()),
				IntTools.toInt(pt2.getY()),
				IntTools.toInt(pt3.getX()),
				IntTools.toInt(pt3.getY()),
				new Bmp.Dot(255, 255, 255, 255)
				);
		canvas.drawLine(
				IntTools.toInt(pt3.getX()),
				IntTools.toInt(pt3.getY()),
				IntTools.toInt(pt1.getX()),
				IntTools.toInt(pt1.getY()),
				new Bmp.Dot(255, 255, 255, 255)
				);
		canvas.fillSameColor(500, 500, new Bmp.Dot(255, 255, 255, 255));
	}

	private void drawTeishi() {
		Canvas canvas = new Canvas(_bmp);

		canvas.fillRect(300, 300, 400, 400, new Bmp.Dot(255, 255, 255, 255));
	}

	private void expand() {
		_bmp = _bmp.expand(50, 50);
	}
}
