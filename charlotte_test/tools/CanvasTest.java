package charlotte_test.tools;

import java.awt.Color;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;

public class CanvasTest {
	public static void main(String[] args) {
		try {
			test01();
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
}
