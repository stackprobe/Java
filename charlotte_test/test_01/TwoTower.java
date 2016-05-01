package charlotte_test.test_01;

import java.awt.Color;

import charlotte.tools.Bmp;
import charlotte.tools.IntTools;
import charlotte.tools.XYPoint;

public class TwoTower {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		Bmp bmp = new Bmp(700, 600);

		XYPoint tower1 = new XYPoint(300, 300);
		XYPoint tower2 = new XYPoint(400, 300);

		for(int x = 0; x < 700; x++) {
			for(int y = 0; y < 600; y++) {
				XYPoint curr = new XYPoint(x, y);
				double d1 = XYPoint.getDistance(curr, tower1);
				double d2 = XYPoint.getDistance(curr, tower2);
				double rate = d1 / d2;

				//rate -= 0.25; // -= TARGET_RATE
				rate -= 0.5; // -= TARGET_RATE
				//rate -= 0.75; // -= TARGET_RATE
				rate = Math.abs(rate);

				int level = 0;

				if(rate < 0.1) {
					rate *= 10.0;
					rate = 1.0 - rate;
					rate *= rate;
					rate *= rate;
					rate *= rate;
					level = IntTools.toInt(rate * 255.0);
					level = IntTools.toRange(level, 0, 255);
				}
				bmp.setDot(x, y, new Bmp.Dot(255, 255, 255 - level, 255 - level));
			}
		}
		bmp.setDot(300, 300, new Bmp.Dot(Color.BLACK));
		bmp.setDot(400, 300, new Bmp.Dot(Color.BLACK));

		bmp.writeToFile("C:/temp/TwoTower.png");
	}
}
