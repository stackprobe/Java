package charlotte_test.tools;

import java.awt.Color;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.DoubleTools;
import charlotte.tools.MathTools;
import charlotte.tools.StringTools;

public class CanvasTest {
	public static void main(String[] args) {
		try {
			test01();
			test02();
			test03();

			System.out.println("OK!");
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
		canvas.drawDouble(152, 152, 2, Color.BLACK, "-1234567890.999");

		for(int c = 0; c < 3; c++) {
			canvas.drawDouble(152, 160 + c * 6, 2, Color.BLUE, "" + Math.random());
		}
		canvas.drawDouble(152, 178, 2, Color.BLUE, "0123456789abcdef.ABCDEF");

		bmp.writeToFile("C:/temp/CanvasTest1a.png");

		// ----

		bmp = new Bmp(400, 200, Color.WHITE);
		canvas = new Canvas(bmp);

		canvas.drawDouble(1, 1, 2, Color.BLACK, StringTools.ALPHA);
		canvas.drawDouble(1, 8, 2, Color.BLUE, StringTools.alpha);

		canvas.drawDouble(1, 15, 4, Color.BLACK, StringTools.ALPHA);
		canvas.drawDouble(1, 25, 4, Color.BLUE, StringTools.alpha);

		canvas.drawDouble(1, 35, 6, Color.BLACK, StringTools.ALPHA);
		canvas.drawDouble(1, 49, 6, Color.BLUE, StringTools.alpha);

		canvas.drawDouble(1, 63, 2, Color.ORANGE, StringTools.PUNCT);

		bmp.writeToFile("C:/temp/CanvasTest1b.png");
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
		canvas.fillSameColor(150, 150, Color.GREEN);
		bmp.setDot(150, 150, new Bmp.Dot(Color.YELLOW));
		bmp.writeToFile("C:/temp/CanvasTest2b.png");

		// ----

		bmp = new Bmp(300, 300, Color.WHITE);
		canvas = new Canvas(bmp);

		canvas.fillRect(50, 50, 200, 200, Color.ORANGE);
		canvas.fillSameColor(150, 150, Color.ORANGE);

		bmp.writeToFile("C:/temp/CanvasTest2c.png");
	}

	private static void test03() throws Exception {
		//*
		final int ROT_DIV = 16;
		/*/
		final int ROT_DIV = 2;
		//*/

		Bmp bmp = new Bmp(1000, 1000, new Color(255, 255, 255, 0));
		Canvas canvas = new Canvas(bmp);

		test03_0(canvas, 950, new Color(120, 150, 120));
		test03_0(canvas, 900, Color.WHITE);
		test03_0(canvas, 850, Color.RED);

		test03_1(canvas);

		bmp = bmp.rotate_ra1();
		canvas = new Canvas(bmp);

		test03_1(canvas);
		test03_2(canvas);

		bmp = bmp.rotate_ra1();
		canvas = new Canvas(bmp);

		test03_2(canvas);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(Math.PI / 4.0 + 0.2, ROT_DIV);

		test03_3(bmp);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(-0.2, ROT_DIV);

		test03_write(bmp, "Error");

		// ----

		bmp = new Bmp(1000, 1000, new Color(255, 255, 255, 0));
		canvas = new Canvas(bmp);

		test03_0(canvas, 950, new Color(120, 150, 120));
		test03_0(canvas, 900, Color.WHITE);
		test03_0(canvas, 850, new Color(255, 235, 0));

		test03_4(canvas,  0, new Color(100, 100, 50));
		test03_4(canvas, 20, Color.WHITE);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(0.2 - 0.05, ROT_DIV);

		test03_3(bmp);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(-0.2, ROT_DIV);

		test03_write(bmp, "Warning");

		// ----

		bmp = new Bmp(1000, 1000, new Color(255, 255, 255, 0));
		canvas = new Canvas(bmp);

		test03_0(canvas, 950, new Color(120, 150, 120));
		test03_0(canvas, 900, Color.WHITE);
		test03_0(canvas, 850, new Color(100, 100, 255));

		test03_4(canvas,  0, new Color(0, 0, 100));
		test03_4(canvas, 20, Color.WHITE);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(Math.PI + 0.2 - 0.05, ROT_DIV);

		test03_3(bmp);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(-0.2, ROT_DIV);

		test03_write(bmp, "Information");

		// ----

		bmp = new Bmp(1000, 1000, new Color(255, 255, 255, 0));
		canvas = new Canvas(bmp);

		test03_0(canvas, 950, new Color(120, 150, 120));
		test03_0(canvas, 900, Color.WHITE);
		test03_0(canvas, 850, new Color(100, 100, 255));

		test03_4(canvas,  0, new Color(0, 0, 100));
		test03_4(canvas, 20, Color.WHITE);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(0.2 - 0.05, ROT_DIV);

		test03_3(bmp);

		bmp.DUMMY_A = 0;
		bmp.DUMMY_R = 255;
		bmp.DUMMY_G = 255;
		bmp.DUMMY_B = 255;
		bmp = bmp.rotate(-0.2, ROT_DIV);

		test03_write(bmp, "Question");
	}

	private static void test03_0(Canvas canvas, int wh, Color color) {
		canvas.drawCircle(500, 500, wh / 2, color);
		canvas.fillSameColor(500, 500, color);
	}

	private static void test03_1(Canvas canvas) {
		canvas.fillRectCenter(500, 500, 700, 250, new Color(128, 0, 0));
	}

	private static void test03_2(Canvas canvas) {
		canvas.fillRectCenter(500, 500, 650, 200, Color.WHITE);
	}

	private static void test03_3(Bmp bmp) {
		for(int x = 0; x < 1000; x++) {
			for(int y = 0; y < 1000; y++) {
				double r = x * y;
				r /= 2500000.0;
				r = 1.0 - r;

				bmp.setR(x, y, DoubleTools.toInt(bmp.getR(x, y) * r));
				bmp.setG(x, y, DoubleTools.toInt(bmp.getG(x, y) * r));
				bmp.setB(x, y, DoubleTools.toInt(bmp.getB(x, y) * r));
			}
		}
	}

	private static void test03_4(Canvas canvas, int minus, Color color) {
		canvas.drawCircle(500, 765, 150 - minus, color);
		canvas.fillSameColor(500, 765, color);
		canvas.fillRectCenter(500, 360, 280 - minus * 2, 490 - minus * 2, color);
	}

	private static void test03_write(Bmp bmp, String kind) throws Exception {
		test03_w1(bmp, kind, 16);
		test03_w1(bmp, kind, 32);
		test03_w1(bmp, kind, 48);
		test03_w1(bmp, kind, 64);
		test03_w1(bmp, kind, 100);
		test03_w1(bmp, kind, 150);
		test03_w1(bmp, kind, 200);
		test03_w1(bmp, kind, 250);
		test03_w1(bmp, kind, 300);
	}

	private static void test03_w1(Bmp bmp, String kind, int dest_wh) throws Exception {
		bmp = bmp.expand(dest_wh, dest_wh);
		bmp.writeToFile("C:/temp/" + kind + "Icon_" + StringTools.zPad(dest_wh, 3) + ".png");
	}
}
