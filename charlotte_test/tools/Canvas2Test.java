package charlotte_test.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas2;
import charlotte.tools.StringTools;
import charlotte.tools.XYPoint;

public class Canvas2Test {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		Bmp bmp = new Bmp(1000, 200, Color.WHITE);
		BufferedImage img = bmp.getBi();
		Graphics g = img.getGraphics();
		Canvas2 c2 = new Canvas2(g);

		c2.drawDouble(1, 1, 2, Color.BLUE, StringTools.ASCII);
		c2.drawDouble(1, 7, 3, Color.DARK_GRAY, StringTools.ASCII);
		c2.drawDouble(1, 15, 4, Color.RED, StringTools.ASCII);

		c2.drawOval(200.0, 100.0, 100.0, 50.0, Color.ORANGE);
		c2.fillOval(200.0, 100.0, 90.0, 40.0, Color.ORANGE);

		c2.fillPolygon(
				new XYPoint[] {
						new XYPoint(330.0, 100.0),
						new XYPoint(360.0, 130.0),
						new XYPoint(360.0, 100.0),
						new XYPoint(330.0, 130.0),
				},
				Color.CYAN
				);

		Bmp.getBmp(img).writeToFile("C:/temp/Camvas2Test.png");
	}
}
