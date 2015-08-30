package charlotte.htt.sample;

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
		return new HttResHtml(String.format(
				"<html>" +
				"<body>" +
				"<h1>リクエストの内容はイカのとおりです。</h1>" +
				"<hr/>" +
				"<table>" +
				"<tr>" +
				"<td>Method</td><td>%s</td>" +
				"<td>URL</td><td>%s</td>" +
				"<td>HTTP version</td><td>%s</td>" +
				"</tr>" +
				"</table>" +
				"</body>" +
				"</html>"
				,req.getMethod()
				,req.getUrlString()
				,req.getHTTPVersion()
				));
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
