package charlotte_test.htt.sample;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResHtml;

public class SimpleHttpService implements HttService {
	public boolean dead;

	@Override
	public boolean interlude() throws Exception {
		return dead == false;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		StringBuffer buff = new StringBuffer();

		buff.append("<html>");
		buff.append("<body>");
		buff.append("<h1>リクエストの内容はイカのとおりです。</h1>");
		buff.append("<hr/>");
		buff.append("<table>");
		buff.append("<tr><td>Client IP address</td><td>");
		buff.append(req.getClientIPAddress());
		buff.append("</td></tr>");
		buff.append("<tr><td>Method</td><td>");
		buff.append(req.getMethod());
		buff.append("</td></tr>");
		buff.append("<tr><td>URL</td><td>");
		buff.append(req.getUrlString());
		buff.append("</td></tr>");
		buff.append("<tr><td>HTTP version</td><td>");
		buff.append(req.getHTTPVersion());
		buff.append("</td></tr>");
		buff.append("</table>");
		buff.append("</html>");

		return new HttResHtml(buff.toString());
	}

	public static void main(String[] args) {
		try {
			final SimpleHttpService service = new SimpleHttpService();

			new Thread() {
				@Override
				public void run() {
					try {
						HttServer.perform(service);
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
}
