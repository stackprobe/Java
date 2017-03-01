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
		_bmp = new Bmp(1000, 1000);

		for(int x = 0; x < 1000; x++) {
			for(int y = 0; y < 1000; y++) {
				_bmp.setDot(x, y, x + y < 1000 ? new Bmp.Dot(255, 255, 255, 255) : new Bmp.Dot(0, 0, 0, 0));
			}
		}
		_bmp = _bmp.expand(95, 30);

		_bmp.writeToFile("C:/temp/volume_mask.png");
		_bmp = new Bmp(1000, 1000);

		drawSquare();
		drawBatsu();
		expand();

		_bmp.writeToFile("C:/temp/button_x.png");
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

	private void drawSquare() {
		Canvas canvas = new Canvas(_bmp);

		canvas.fillRect(100, 100, 800, 100, new Bmp.Dot(255, 255, 255, 255));
		canvas.fillRect(100, 100, 100, 800, new Bmp.Dot(255, 255, 255, 255));
		canvas.fillRect(100, 800, 800, 100, new Bmp.Dot(255, 255, 255, 255));
		canvas.fillRect(800, 100, 100, 800, new Bmp.Dot(255, 255, 255, 255));
	}

	private void drawBatsu() {
		Canvas canvas = new Canvas(_bmp);

		for(int c = 0; c < 700; c++) {
			canvas.fillRect(100 + c, 100 + c, 70, 70, new Bmp.Dot(255, 255, 255, 255));
			canvas.fillRect(830 - c, 100 + c, 70, 70, new Bmp.Dot(255, 255, 255, 255));

			/*
			canvas.fillRect(100 + c, 100 + c, 100, 100, new Bmp.Dot(255, 255, 255, 255));
			canvas.fillRect(800 - c, 100 + c, 100, 100, new Bmp.Dot(255, 255, 255, 255));
			*/

			/*
			canvas.fillRect(100 + c, 100 + c, 50, 50, new Bmp.Dot(255, 255, 255, 255));
			canvas.fillRect(850 - c, 100 + c, 50, 50, new Bmp.Dot(255, 255, 255, 255));
			*/
		}
	}

	private void expand() {
		_bmp = _bmp.expand(50, 50);
	}
}
