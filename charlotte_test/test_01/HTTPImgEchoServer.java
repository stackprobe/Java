package charlotte_test.test_01;

import java.awt.Color;
import java.util.List;

import javax.swing.JDialog;

import charlotte.tools.Bmp;
import charlotte.tools.Canvas;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPServer;
import charlotte.tools.StringTools;

public class HTTPImgEchoServer {
	public static void main(String[] args) {
		try {
			final JDialog dlg = new JDialog();

			dlg.setModal(false);
			dlg.setVisible(true);

			new HTTPServer() {
				@Override
				protected void recved(Connection con) throws Exception {
					String tmp = con.path.substring(1, con.path.indexOf('.'));
					List<String> tmp2 = StringTools.tokenize(tmp, "/");

					String file = "C:/tmp/CTL32_road/road/" + tmp2.get(0) + "/" + tmp2.get(2) + "/" + tmp2.get(1) + ".png";

					Bmp bmp;

					if(FileTools.exists(file)) {
						bmp = Bmp.fromFile(file);
					}
					else {
						bmp = new Bmp(64, 64, Color.ORANGE);
						Canvas canvas = new Canvas(bmp);
						canvas.drawDouble(1, 1, 2, Color.BLUE, tmp2.get(0));
						canvas.drawDouble(1, 8, 2, Color.BLUE, tmp2.get(1));
						canvas.drawDouble(1, 15, 2, Color.BLUE, tmp2.get(2));
						bmp = bmp.expand(256, 256);
					}

					System.out.println("*" + con.path);

					con.resHeaderFields.put("Content-Type", "image/png");
					con.resBody = bmp.getBytes();
				}

				@Override
				protected boolean interlude() throws Exception {
					boolean ret = dlg.isVisible();
					//System.out.println("interrupt: " + ret);
					return ret;
				}
			}
			.perform();

			dlg.dispose();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
