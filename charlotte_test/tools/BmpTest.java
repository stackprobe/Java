package charlotte_test.tools;

import java.awt.Color;
import java.awt.Font;

import charlotte.tools.Bmp;
import charlotte.tools.BmpTools;

public class BmpTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
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
	}

	private static void test02() throws Exception {
		Bmp g = BmpTools.getStringBmp(
				"日本語abc123",
				Color.BLACK,
				Color.WHITE,
				"メイリオ",
				Font.PLAIN,
				120,
				1000, 200, 50, 100, 3, 10
				);
		g.writeToFile("C:/temp/getStringBmpTest.png");
	}
}
