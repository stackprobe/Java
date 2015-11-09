package charlotte_test.tools;

import javax.swing.JDialog;

import charlotte.tools.HTTPServer;
import charlotte.tools.StringTools;

public class HTTPServerTest {
	public static void main(String[] args) {
		try {
			final JDialog dlg = new JDialog();

			dlg.setModal(false);
			dlg.setVisible(true);

			new HTTPServer() {
				@Override
				protected void recved(Connection con) throws Exception {
					System.out.println("method: " + con.method);
					System.out.println("url: " + con.path);
					System.out.println("verPart: " + con.httpVersion);

					for(String name : con.req.getHeaderFields().keySet()) {
						System.out.println("header: " + name + " = " + con.req.getHeaderFields().get(name));
					}
					System.out.println("body_length: " + con.req.getBody().length);

					con.resHeaderFields.put("Content-Type", "text/html charset=UTF-8");
					con.resBody =
							"<html><body><h1>Drink HUB ALE!</h1></body></html>"
							.getBytes(StringTools.CHARSET_UTF8);
				}

				@Override
				protected boolean interlude() throws Exception {
					boolean ret = dlg.isVisible();
					System.out.println("interrupt: " + ret);
					return ret;
				}
			}
			.listen();

			dlg.dispose();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
