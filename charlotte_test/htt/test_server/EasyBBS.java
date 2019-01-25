package charlotte_test.htt.test_server;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResHtml;
import charlotte.tools.DateTimeToSec;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPServer;
import charlotte.tools.ObjectMap;
import charlotte.tools.SecurityTools;
import charlotte.tools.StringTools;
import charlotte.tools.TimeData;

public class EasyBBS implements HttService {
	public static void main(String[] args) {
		try {
			final EasyBBS service = new EasyBBS();

			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						System.out.println("簡易BBSサーバーを開始しました。");
						HttServer.perform(service);
						System.out.println("簡易BBSサーバーは停止しました。");
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};

			th.start();

			JOptionPane.showMessageDialog(
					null,
					"停止するには「了解(OK)」を押して下さい。",
					"簡易BBSサーバー実行中です",
					JOptionPane.INFORMATION_MESSAGE
					);

			service.dead = true;

			th.join();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public boolean dead;

	@Override
	public boolean interlude() throws Exception {
		return dead == false;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		if(req.getUrl().getPath().equals("/remark")) {
			byte[] body = req.getBodyPart();
			String sBody = new String(body, StringTools.CHARSET_ASCII);
			ObjectMap q = HTTPServer.parseQuery(sBody); // POSTなのでBodyから

			String user = q.getString("user");
			String eMailAddress = q.getString("e-mail");
			String message = q.getString("message");

			user = user.trim();
			user = cutTrail(user, 100);
			eMailAddress = eMailAddress.trim();
			eMailAddress = cutTrail(eMailAddress, 100);
			message = message.trim();
			message = cutTrail(message, 500);

			if(user.isEmpty()) {
				user = "anonymous";
			}
			if(message.isEmpty()) {
				message = "(silent)";
			}
			RemarkInfo remark = new RemarkInfo();

			remark.dateTime = DateTimeToSec.Now.getDateTime();
			remark.ipAddress = req.getClientIPAddress();
			remark.user = user;
			remark.eMailAddress = eMailAddress;
			remark.message = message;

			if(1000 < remarks.size()) {
				remarks.remove(0);
			}
			remarks.add(remark);

			String html = new String(FileTools.readToEnd(this.getClass().getResource("res/EasyBBS_Remarked.html")), StringTools.CHARSET_UTF8);

			html = html.replace("${USER}", encode(user));
			html = html.replace("${E-MAIL}", encode(eMailAddress));

			return new HttResHtml(html);
		}
		else {
			String user = "anonymous@" + SecurityTools.cRandom(10000);
			String eMailAddress = "sage";

			String query = req.getUrl().getQuery(); // GETなのでUrlから

			if(query != null) {
				ObjectMap q = HTTPServer.parseQuery(query);

				user = q.getString("user", user);
				eMailAddress = q.getString("e-mail", eMailAddress);
			}
			String html = new String(FileTools.readToEnd(this.getClass().getResource("res/EasyBBS_Main.html")), StringTools.CHARSET_UTF8);

			html = html.replace("${USER}", encode(user));
			html = html.replace("${E-MAIL}", encode(eMailAddress));
			html = html.replace("${TIMELINE}", getTimeline());

			return new HttResHtml(html);
		}
	}

	private String getTimeline() throws Exception {
		StringBuffer html = new StringBuffer();
		String format = new String(FileTools.readToEnd(this.getClass().getResource("res/EasyBBS_Remark.html")), StringTools.CHARSET_UTF8);

		for(RemarkInfo remark : remarks) {
			String text = format;

			text = text.replace("${DATE-TIME}", TimeData.fromTimeStamp(remark.dateTime).getString("Y/M/D (W) h:m:s"));
			text = text.replace("${IP}", remark.ipAddress);
			text = text.replace("${USER}", encode(remark.user));
			text = text.replace("${E-MAIL}", encode(remark.eMailAddress));
			text = text.replace("${MESSAGE}", encodeMultiLineText(remark.message));

			html.append(text);
		}
		return html.toString();
	}

	private class RemarkInfo {
		public long dateTime;
		public String ipAddress;
		public String user;
		public String eMailAddress;
		public String message;
	}

	private List<RemarkInfo> remarks = new ArrayList<RemarkInfo>();

	private static String encodeMultiLineText(String text) {
		text = text.replace("\r\n", "\n");
		text = text.replace("\r", "");

		String[] lines = text.split("[\n]");

		for(int index = 0; index < lines.length; index++) {
			lines[index] = encode(lines[index]);
		}
		return String.join("<br/>", lines);
	}

	private static String encode(String str) {
		StringBuffer buff = new StringBuffer();

		for(char chr : str.toCharArray()) {
			if(StringTools.contains(StringTools.DIGIT + StringTools.ALPHA + StringTools.alpha, chr) || 0x00ff < chr) {
				buff.append(chr);
			}
			else {
				buff.append(String.format("&#x00%02x;", chr & 0xff));
			}
		}
		return buff.toString();
	}

	private static String cutTrail(String str, int maxlen) {
		if(maxlen < str.length()) {
			str = str.substring(0, maxlen);
		}
		return str;
	}
}
