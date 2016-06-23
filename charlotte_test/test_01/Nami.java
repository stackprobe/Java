package charlotte_test.test_01;

import java.awt.Color;

import charlotte.tools.Bmp;
import charlotte.tools.StringTools;

public class Nami {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			doHako2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		doNami(
				"C:/var/_image.png",
				"C:/temp/nami_test01.png",
				1.0, 0.03, 5.0,
				2.0, 0.05, 7.0,
				4,
				new Bmp.Dot(Color.ORANGE)
				);
	}

	private static void test02() throws Exception {
		doNami2(
				"C:/var/_image.png",
				"C:/temp/nami_test02_", ".png",
				10, 1, -1,
				1.0, 0.03, 5.0,
				2.0, 0.05, 7.0,
				4,
				null//new Bmp.Dot(Color.ORANGE)
				);
	}

	/**
	 * Hako2 Floor2
	 * @throws Exception
	 */
	private static void doHako2() throws Exception {
		{
			Bmp bmp = Bmp.fromFile("C:/Dev/Game/Hako2/Resource/ぱくたそ/140726167640~1000.jpg");
			bmp.expand(640, 480);
			bmp.writeToFile("C:/temp/hako2.png");
		}

		doNami2(
				"C:/temp/hako2.png",
				"C:/Dev/Game/Hako2/Resource/ぱくたそ/floor2_nami/floor2_nami_", ".png",
				16, 1, 1,
				0.0, 0.013, 13.0,
				0.0, 0.017, 11.0,
				4, null
				);
		/*
		doNami2(
				"C:/temp/hako2.png",
				"C:/Dev/Game/Hako2/Resource/ぱくたそ/floor2_nami/floor2_nami_", ".png",
				16, 1, 1,
				0.0, 0.003, 7.0,
				0.0, 0.007, 7.0,
				4, null
				);
				*/
	}

	/**
	 *
	 * @param rFile
	 * @param wFilePrefix
	 * @param wFileSuffix
	 * @param komanum 1 to IMAX
	 * @param xStartRotAddSign -1 or 1
	 * @param yStartRotAddSign -1 or 1
	 * @param xStartRot 0.0 to 2*PI
	 * @param xRotAdd 0.0 to 2*PI
	 * @param xRate -IMAX to IMAX
	 * @param yStartRot 0.0 to 2*PI
	 * @param yRotAdd 0.0 to 2*PI
	 * @param yRate -IMAX to IMAX
	 * @param div 1 to IMAX
	 * @param outerDot null == loop
	 * @throws Exception
	 */
	private static void doNami2(String rFile, String wFilePrefix, String wFileSuffix,
			int komanum, int xStartRotAddSign, int yStartRotAddSign,
			double xStartRot, double xRotAdd, double xRate,
			double yStartRot, double yRotAdd, double yRate,
			int div,
			Bmp.Dot outerDot
			) throws Exception {
		for(int c = 0; c < komanum; c++) {
			System.out.println("" + c);

			doNami(rFile, wFilePrefix + StringTools.zPad(c, 3) + wFileSuffix,
					xStartRot + c * (2.0 * Math.PI * xStartRotAddSign) / komanum, xRotAdd, xRate,
					yStartRot + c * (2.0 * Math.PI * yStartRotAddSign) / komanum, yRotAdd, yRate,
					div,
					outerDot
					);
		}
	}

	/**
	 *
	 * @param rFile
	 * @param wFile
	 * @param xStartRot 0.0 to 2*PI
	 * @param xRotAdd 0.0 to 2*PI
	 * @param xRate -IMAX to IMAX
	 * @param yStartRot 0.0 to 2*PI
	 * @param yRotAdd 0.0 to 2*PI
	 * @param yRate -IMAX to IMAX
	 * @param div 1 to IMAX
	 * @param outerDot null == loop
	 * @throws Exception
	 */
	private static void doNami(String rFile, String wFile,
			double xStartRot, double xRotAdd, double xRate,
			double yStartRot, double yRotAdd, double yRate,
			int div,
			Bmp.Dot outerDot
			) throws Exception {
		Bmp src = Bmp.fromFile(rFile);
		Bmp dest = new Bmp(src.getWidth(), src.getHeight());

		for(int x = 0; x < src.getWidth(); x++) {
			for(int y = 0; y < src.getHeight(); y++) {
				Bmp.Dot[] dots = new Bmp.Dot[div * div];
				int c = 0;

				for(int sx = 0; sx < div; sx++) {
					for(int sy = 0; sy < div; sy++) {
						double dx = x + (sx + 0.5) / div;
						double dy = y + (sy + 0.5) / div;

						double dPos = dx * xRotAdd + dy * yRotAdd;
						double dDiffX = Math.sin(xStartRot + dPos) * xRate;
						double dDiffY = Math.cos(yStartRot + dPos) * yRate;

						dx += dDiffX;
						dy += dDiffY;

						int ix = (int)dx;
						int iy = (int)dy;

						// ? loop
						if(outerDot == null) {
							ix %= src.getWidth();
							ix += src.getWidth();
							ix %= src.getWidth();

							iy %= src.getHeight();
							iy += src.getHeight();
							iy %= src.getHeight();

							dots[c++] = src.getDot(ix, iy);
						}
						else if(
								0 <= ix && ix < src.getWidth() &&
								0 <= iy && iy < src.getHeight()
								) {
							dots[c++] = src.getDot(ix, iy);
						}
						else {
							dots[c++] = outerDot;
						}
					}
				}
				dest.setDot(x, y, Bmp.mix(dots));
			}
		}
		dest.writeToFile(wFile);
	}
}
