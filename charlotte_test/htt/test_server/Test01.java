package charlotte_test.htt.test_server;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResHtml;

public class Test01 implements HttService {
	public static void main(String[] args) {
		try {
			final Test01 service = new Test01();

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

			JOptionPane.showMessageDialog(
					null,
					"停止するには「了解(OK)」を押して下さい。",
					"サーバー実行中です",
					JOptionPane.INFORMATION_MESSAGE
					);

			service.dead = true;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public boolean dead;

	@Override
	public boolean interlude() throws Exception {
		return dead == false;
	}

	private StringBuffer _buff;

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		_buff = new StringBuffer();

		_buff.append("<html>");
		_buff.append("<body>");
		_buff.append("<table border=\"1\">");

		addTr("クライアントのIPアドレス", req.getClientIPAddress());
		addTr("Method", req.getMethod());
		addTr("Url", req.getUrl().toString());
		addTr("HTTP_Version", req.getHTTPVersion());

		for(String headerKey : req.getHeaderFields().keySet())
		{
			addTr("Header_" + headerKey, req.getHeaderFields().get(headerKey));
		}
		addTr("Body", toAsciiString(req.getBodyPart()));

		_buff.append("</table>");
		_buff.append("</body>");
		_buff.append("</html>");

		return new HttResHtml(_buff.toString());
	}

	private void addTr(String title, String value) {
		_buff.append("<tr>");
		_buff.append("<td>");
		_buff.append(title);
		_buff.append("</td>");
		_buff.append("<td>");
		_buff.append(value);
		_buff.append("</td>");
		_buff.append("</tr>");
	}

	private static String toAsciiString(byte[] data) {
		try {
			return new String(data, "US-ASCII");
		}
		catch(Throwable e) {
			return e.getMessage();
		}
	}
}
