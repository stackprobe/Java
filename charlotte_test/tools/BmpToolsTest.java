package charlotte_test.tools;

import java.awt.Color;
import java.awt.Font;

import charlotte.tools.Bmp;
import charlotte.tools.BmpTools;

public class BmpToolsTest {
	public static void main(String[] args) {
		try {
			main2();
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
					50,
					50,
					1,
					0
					);

			System.out.println(fontSize + ": " + bmp.getWidth() + ", " + bmp.getHeight());
		}
	}
}
