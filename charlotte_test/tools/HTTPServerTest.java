package charlotte_test.tools;

import javax.swing.JDialog;

import charlotte.tools.ExtToContentType;
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

					{
						String contentType = con.req.getHeaderFields().get("Content-Type");

						if(contentType != null) {
							ExtToContentType.Info info = ExtToContentType.contentTypeToInfo(contentType);

							if("txt".equals(info.ext)) {
								System.out.println("body_charset: " + info.charset);
								System.out.println("body_text: " + new String(con.req.getBody(), info.charset));
							}
						}
					}

					con.resHeaderFields.put("Content-Type", "text/html charset=UTF-8");
					con.resBody =
							"<html><body><h1>Drink HUB ALE!</h1></body></html>"
							.getBytes(StringTools.CHARSET_UTF8);
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
		System.exit(0);
	}
}
