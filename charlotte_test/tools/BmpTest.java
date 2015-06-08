package charlotte_test.tools;

import charlotte.tools.Bmp;

public class BmpTest {
	public static void main(String[] args) {
		try {
			Bmp g = Bmp.fromFile("C:/etc/画像/壁紙/1423965337569.jpg");
			Bmp s = Bmp.fromFile("C:/etc/画像/ネタ/2130185.png");

			g.paste(s);
			g.writeToFile("C:/temp/test.png");
			g = g.expand(3000, 500);
			g.writeToFile("C:/temp/test_02.png");

			/*
			g = Bmp.fromFile("C:/etc/画像/1321690914158.jpg");
			g = g.rotateLq(0.5);
			g.writeToFile("C:/temp/test_03.png");

			g = Bmp.fromFile("C:/etc/画像/ネタ/48490430_p0.jpg");
			g = g.rotate(-0.5);
			g.writeToFile("C:/temp/test_04.png");
			*/

			g = Bmp.fromFile("C:/etc/画像/ネタ/48745783_p0.jpg");
			g = g.rotateLq(0.5);
			g.writeToFile("C:/temp/test_03.png");

			g = Bmp.fromFile("C:/etc/画像/twitterアイコン/まどマギ/icon_mami.png");
			g = g.rotate(0.5);
			g.writeToFile("C:/temp/mami.png");

			g = Bmp.fromFile("C:/etc/画像/twitterアイコン/まどマギ/icon_mami.png");
			System.out.println("" + g.getDot(18, 0));
			//g = g.rotate(0.0);
			g.writeToFile("C:/temp/mami2.png");

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
