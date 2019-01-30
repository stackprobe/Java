package charlotte_test.htt.test_server;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttRes301;
import charlotte.htt.response.HttRes404;
import charlotte.htt.response.HttResFile;
import charlotte.htt.response.HttResHtml;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPServer;
import charlotte.tools.StringTools;

public class LiteFiler implements HttService {
	public static void main(String[] args) {
		try {
			final LiteFiler service = new LiteFiler();

			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						System.out.println("LiteFiler started.");
						HttServer.perform(service);
						System.out.println("LiteFiler stopped.");
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};

			th.start();

			JOptionPane.showMessageDialog(
					null,
					"Press [OK] button to stop the server.",
					"LiteFiler running.",
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

	private static final String ROOT_DIR = "C:/";

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		String reqPath = HTTPServer.decodeUrl(req.getUrl().getPath(), StringTools.CHARSET_UTF8);
		List<String> pTkns = StringTools.tokenize(reqPath, "/", false, true);
		String path = String.join("/", pTkns);

		path = FileTools.combine(ROOT_DIR, path);

		File f = new File(path);

		if(f.isFile()) {
			return new HttResFile(f);
		}

		if(f.isDirectory()) {
			if(reqPath.endsWith("/") == false) {
				return new HttRes301(HTTPServer.encodeUrl(reqPath + "/", StringTools.CHARSET_UTF8));
			}
			File[] subFs = f.listFiles();

			Arrays.sort(subFs, (a, b) -> {
				int da = a.isDirectory() ? 0 : 1;
				int db = b.isDirectory() ? 0 : 1;
				int ret = da - db;

				if(ret == 0) {
					ret = StringTools.compIgnoreCase.compare(a.getName(), b.getName());
				}
				return ret;
			});

			StringBuffer html = new StringBuffer();

			html.append("<html>");
			html.append("<body>");
			html.append("<h1>");
			html.append(path);
			html.append("</h1>");
			html.append("<table border='1'>");
			html.append("<tr>");
			html.append("<th>name</th>");
			html.append("<th>isDirectory</th>");
			html.append("<th>size</th>");
			html.append("<th>lastUpdateTime</th>");
			html.append("</tr>");

			if(1 <= pTkns.size()) {
				html.append("<tr>");
				html.append("<td><a href='..'>&lt;Parent Directory&gt;</a></td>");
				html.append("<td></td>");
				html.append("<td></td>");
				html.append("<td></td>");
				html.append("</tr>");
			}

			for(File subF : subFs) {
				html.append("<tr>");
				html.append("<td>");
				html.append("<a href='");
				html.append(HTTPServer.encodeUrl(subF.getName(), StringTools.CHARSET_UTF8));
				html.append("'>");
				html.append(subF.getName());
				html.append("</a>");
				html.append("</td>");
				html.append("<td>");
				html.append(subF.isDirectory());
				html.append("</td>");

				if(subF.isDirectory()) {
					html.append("<td></td>");
					html.append("<td></td>");
				}
				else {
					html.append("<td>");
					html.append(subF.length());
					html.append("</td>");
					html.append("<td>");
					html.append("POSIX ");
					html.append(subF.lastModified());
					html.append(" milliseconds");
					html.append("</td>");
				}
				html.append("</tr>");
			}
			html.append("</table>");
			html.append("</body>");
			html.append("</html>");

			return new HttResHtml(html.toString());
		}

		return new HttRes404();
	}
}
