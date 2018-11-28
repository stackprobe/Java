package charlotte_test.htt.test_server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResFileImage;
import charlotte.htt.response.HttResHtml;

public class TestHtmlJpegPngJsonXml implements HttService {
	public static void main(String[] args) {
		try {
			final TestHtmlJpegPngJsonXml service = new TestHtmlJpegPngJsonXml();

			new Thread() {
				@Override
				public void run() {
					try {
						System.out.println("サーバーを開始しました。");
						HttServer.perform(service);
						System.out.println("サーバーは停止しました。");
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			}
			.start();

			System.out.println("サーバーを停止するには、エンターキーを押して下さい。");
			System.in.read();
			System.out.println("サーバーを停止します。");

			service.alive = false;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private boolean alive = true;

	@Override
	public boolean interlude() throws Exception {
		return alive;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		String path = req.getUrl().getPath();

		if("/test0001.html".equals(path)) {
			StringBuffer buff = new StringBuffer();

			buff.append("<html>");
			buff.append("<body>");
			buff.append("This is test0001.html<br/>");
			buff.append("<h1>☃☃☃☃☃</h1>");
			buff.append("<h1>☃☃☃☃☃</h1>");
			buff.append("<h1>☃☃☃☃☃</h1>");
			buff.append("<a href=\"/\">return</a>");
			buff.append("</body>");
			buff.append("</html>");

			return new HttResHtml(buff.toString());
		}
		else if("/test0002.jpeg".equals(path)) {
			BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = bi.createGraphics();

			g.setColor(Color.ORANGE);
			g.fillRect(0, 0, 300, 300);

			g.setColor(Color.YELLOW);
			g.fillOval(50, 50, 200, 200);

			g.setColor(Color.BLUE);
			g.setFont(new Font("メイリオ", Font.PLAIN, 100));
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.drawString("☃", 100, 180); // center ???

			ByteArrayOutputStream mem = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpeg", new BufferedOutputStream(mem));
			byte[] jpegImage = mem.toByteArray();

			return new HttResFileImage(jpegImage, "*.jpeg");
		}
		else if("/test0003.png".equals(path)) {
			BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = bi.createGraphics();

			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 800, 600);

			Random rnd = new Random();

			for(int c = 0; c < 10; c++) {
				g.setColor(new Color(
						rnd.nextInt(256),
						rnd.nextInt(256),
						rnd.nextInt(256)
						));

				g.fillRect(
						rnd.nextInt(400),
						rnd.nextInt(300),
						10 + rnd.nextInt(400 - 10),
						10 + rnd.nextInt(300 - 10)
						);
			}

			ByteArrayOutputStream mem = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", new BufferedOutputStream(mem));
			byte[] pngImage = mem.toByteArray();

			return new HttResFileImage(pngImage, "*.png");
		}
		else if("/test0004.json".equals(path)) {
			String[] lines = new String[] {
					"{",
					"\t\"emotion\": \"nothing\"",
					"}",
			};

			String text = String.join("\r\n", lines) + "\r\n";

			return new HttResFileImage(text.getBytes("UTF-8"), "*.json");
		}
		else if("/test0005.xml".equals(path)) {
			String[] lines = new String[] {
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
					"<root>",
					"\t<key>emotion</key>",
					"\t<value>nothing</value>",
					"</root>",
			};

			String text = String.join("\r\n", lines) + "\r\n";

			return new HttResFileImage(text.getBytes("UTF-8"), "*.xml");
		}

		{
			StringBuffer buff = new StringBuffer();

			buff.append("<html>");
			buff.append("<body>");
			buff.append("test page<br/>");
			buff.append("<a href=\"/test0001.html\">/test0001.html</a><br/>");
			buff.append("<a href=\"/test0002.jpeg\">/test0002.jpeg</a><br/>");
			buff.append("<a href=\"/test0003.png\">/test0003.png</a><br/>");
			buff.append("<a href=\"/test0004.json\">/test0004.json</a><br/>");
			buff.append("<a href=\"/test0005.xml\">/test0005.xml</a><br/>");
			buff.append("</body>");
			buff.append("</html>");

			return new HttResHtml(buff.toString());
		}
	}
}
