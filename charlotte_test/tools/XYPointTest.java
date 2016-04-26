package charlotte_test.tools;

import charlotte.tools.Bmp;
import charlotte.tools.IntTools;
import charlotte.tools.XYPoint;

public class XYPointTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		final int W = 1000;
		final int H = 1000;
		final int ORIGIN_X = 500;
		final int ORIGIN_Y = 500;

		Bmp bmp = new Bmp(W, H);

		for(int x = 0; x < W; x++) {
			for(int y = 0; y < H; y++) {
				double angle = XYPoint.getAngle(new XYPoint(x - ORIGIN_X, y - ORIGIN_Y));
				angle /= 7.0;
				int v = IntTools.toInt(angle * 255.0);
				bmp.setDot(x, y, new Bmp.Dot(255, v, v, v));
			}
		}
		bmp.writeToFile("C:/temp/XYPointTest.png");
	}
}
