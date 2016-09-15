package charlotte.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public static class AsciiStringBmp {
		private Map<String, Bmp> _bmps = MapTools.<Bmp>create();
		private Color _backColor;

		public AsciiStringBmp(Color backColor, Color textColor, String fontName, int fontStyle, int fontSize, int bi_w, int bi_h, int ds_l, int ds_t, int zoom, int margin) {
			for(char chr : StringTools.ASCII.toCharArray()) {
				_bmps.put("" + chr, BmpTools.getStringBmp("" + chr, backColor, textColor, fontName, fontStyle, fontSize, bi_w, bi_h, ds_l, ds_t, zoom, margin));
			}
			_backColor = backColor;
		}

		public Bmp getStringBmp(String str) {
			List<Bmp> bmps = new ArrayList<Bmp>();
			int w = -1;
			int h = -1;
			int spcW = _bmps.get("-").getWidth();

			for(char chr : str.toCharArray()) {
				Bmp bmp = _bmps.get("" + chr);

				if(bmp != null) {
					bmps.add(bmp);
					w += bmp.getWidth();
					h = Math.max(h, bmp.getHeight());
				}
				else {
					bmps.add(null);
					w += spcW;
				}
			}
			Bmp ret = new Bmp(w, h, _backColor);
			int x = 0;

			for(Bmp bmp : bmps) {
				if(bmp != null) {
					ret.paste(bmp, x, h - bmp.getHeight());
					x += bmp.getWidth();
				}
				else {
					x += spcW;
				}
			}
			return ret;
		}
	}
}
