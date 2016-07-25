package charlotte.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BmpTools {
	public static Bmp getStringBmp(String str, Color backColor, Color textColor, String fontName, int fontStyle, int fontSize, int bi_w, int bi_h, int ds_l, int ds_t, int zoom, int margin) {
		if(ds_l == -1) {
			ds_l = fontSize + margin;
		}
		if(ds_t == -1) {
			ds_t = bi_h - fontSize - margin;
		}

		BufferedImage bi = new BufferedImage(bi_w, bi_h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = bi.createGraphics();

		g.setColor(backColor);
		g.fillRect(0, 0, bi_w, bi_h);

		g.setColor(textColor);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(str, ds_l, ds_t);

		Bmp bmp = Bmp.getBmp(bi);
		bmp = bmp.expand(bmp.getWidth() / zoom, bmp.getHeight() / zoom);
		bmp = bmp.trim(new Bmp.Dot(backColor), margin);
		return bmp;
	}
}
