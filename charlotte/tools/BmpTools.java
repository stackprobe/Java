package charlotte.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BmpTools {
	public static Bmp getStringBmp(String str, Color backColor, Color textColor, String fontName, int fontStyle, int fontSize, int bi_w, int bi_h, int ds_l, int ds_t, int zoom, int margin) {
		BufferedImage bi = new BufferedImage(bi_w, bi_h, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bi.createGraphics();

		g.setColor(backColor);
		g.fillRect(0, 0, bi_w, bi_h);

		g.setColor(textColor);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(str, ds_l, ds_t);

		Bmp bmp = Bmp.getBmp(bi);
		bmp = bmp.expand(bmp.getWidth() / zoom, bmp.getHeight() / zoom);
		System.out.println("bmpSize_1: " + bmp.getWidth() + ", " + bmp.getHeight()); // test
		bmp = bmp.trim(new Bmp.Dot(255, backColor.getRed(), backColor.getGreen(), backColor.getBlue()), margin);
		System.out.println("bmpSize_2: " + bmp.getWidth() + ", " + bmp.getHeight()); // test
		return bmp;
	}
}
