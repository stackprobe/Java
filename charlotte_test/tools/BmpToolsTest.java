package charlotte_test.tools;

import java.awt.Color;
import java.awt.Font;

import charlotte.tools.Bmp;
import charlotte.tools.BmpTools;
import charlotte.tools.StringTools;

public class BmpToolsTest {
	public static void main(String[] args) {
		try {
			//main2();
			main3();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void main2() throws Exception {
		for(double fontSize = 10.0; fontSize < 11.0; fontSize += 0.1) {
			Bmp bmp = BmpTools.getStringBmp(
					"日本語abc123@/.あいうえおカキクケコ＠＠＠",
					Color.WHITE,
					Color.BLACK,
					"ＭＳ ゴシック",
					Font.PLAIN,
					(int)fontSize,
					1000,
					100,
					-1,
					-1,
					1,
					0
					);

			System.out.println(fontSize + ": " + bmp.getWidth() + ", " + bmp.getHeight());

			bmp.writeToFile("C:/temp/1.png");
		}
	}

	private static void main3() throws Exception {
		BmpTools.AsciiStringBmp asBmp = new BmpTools.AsciiStringBmp(
				new Color(0, true),
				Color.BLACK,
				"メイリオ",
				Font.BOLD,
				120,
				300,
				300,
				-1,
				-1,
				4,
				2
				);

		Bmp bmp = asBmp.getStringBmp(StringTools.ASCII_SPC);
		bmp.writeToFile("C:/temp/2.png");

		bmp = asBmp.getStringBmp("_");
		bmp.writeToFile("C:/temp/2-2.png");

		bmp = asBmp.getStringBmp("-_");
		bmp.writeToFile("C:/temp/2-3.png");

		bmp = asBmp.getStringBmp("^-_");
		bmp.writeToFile("C:/temp/2-4.png");

		bmp = asBmp.getStringBmp("^-");
		bmp.writeToFile("C:/temp/2-5.png");

		bmp = asBmp.getStringBmp("^");
		bmp.writeToFile("C:/temp/2-6.png");

		asBmp = new BmpTools.AsciiStringBmp(
				new Color(0, true),
				Color.BLACK,
				"Impact",
				Font.BOLD,
				120,
				300,
				300,
				-1,
				-1,
				4,
				2,
				1
				);

		bmp = asBmp.getStringBmp(StringTools.ASCII_SPC);
		bmp.writeToFile("C:/temp/2-7.png");
	}
}
