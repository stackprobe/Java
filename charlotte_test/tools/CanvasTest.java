package charlotte_test.tools;

import java.awt.Color;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.MathTools;

public class CanvasTest {
	public static void main(String[] args) {
		try {
			test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		Bmp bmp = new Bmp(300, 300, Color.WHITE);
		Canvas canvas = new Canvas(bmp);

		canvas.fillRect(10, 10, 140, 140, Color.PINK);
		canvas.drawRect(10, 10, 280, 280, Color.GRAY);

		canvas.drawCircle(150, 150, 100, Color.RED);
		canvas.drawLine(150, 50, 150, 250, Color.RED);
		canvas.drawLine(150, 150, 250, 150, Color.RED);
		canvas.drawDouble(152, 152, 1, Color.BLACK, "-1234567890.999");

		for(int c = 0; c < 3; c++) {
			canvas.drawDouble(152, 160 + c * 6, 1, Color.BLUE, "" + Math.random());
		}
		bmp.writeToFile("C:/temp/CanvasTest.png");
	}

	private static void test02() throws Exception {
		Bmp bmp = new Bmp(300, 300, Color.WHITE);
		Canvas canvas = new Canvas(bmp);

		for(int c = 0; c < 10; c++) {
			int x1 = MathTools.random(300);
			int y1 = MathTools.random(300);
			int x2 = MathTools.random(300);
			int y2 = MathTools.random(300);

			canvas.drawLine(x1, y1, x2, y2, Color.BLUE);
		}
		for(int c = 0; c < 5; c++) {
			int x = MathTools.random(300);
			int y = MathTools.random(300);
			int r = MathTools.random(300);

			canvas.drawCircle(x, y, r, Color.RED);
		}
		bmp.writeToFile("C:/temp/CanvasTest2a.png");
		canvas.paste(150, 150, Color.GREEN);
		bmp.setDot(150, 150, new Bmp.Dot(Color.YELLOW));
		bmp.writeToFile("C:/temp/CanvasTest2b.png");

		// ----

		bmp = new Bmp(300, 300, Color.WHITE);
		canvas = new Canvas(bmp);

		canvas.fillRect(50, 50, 200, 200, Color.ORANGE);
		canvas.paste(150, 150, Color.ORANGE);

		bmp.writeToFile("C:/temp/CanvasTest2c.png");
	}
}
