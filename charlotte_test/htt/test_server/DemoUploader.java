package charlotte_test.htt.test_server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResFileImage;
import charlotte.htt.response.HttResHtml;
import charlotte.tools.ExtToContentType;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPServer;
import charlotte.tools.MapTools;
import charlotte.tools.StringTools;

public class DemoUploader implements HttService {
	public static void main(String[] args) {
		try {
			final DemoUploader service = new DemoUploader();

			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						System.out.println("DemoUploader started.");
						HttServer.perform(service);
						System.out.println("DemoUploader stopped.");
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
					"DemoUploader running.",
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

	private byte[] _lastUploadedFileData;
	private String _lastUploadedFile;

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		if(req.getUrl().getPath().startsWith("/uploaded-file/")) {
			return new HttResFileImage(_lastUploadedFileData, _lastUploadedFile);
		}

		if(req.getUrl().getPath().equals("/upload")) {
			byte[] body = req.getBodyPart();

			MultiPartFormData mpfd = new MultiPartFormData();
			mpfd.load(body);

			MultiPartContent uploadFile = mpfd.get("upload-file");
			MultiPartContent supplement = mpfd.get("supplement");

			String html = new String(FileTools.readToEnd(this.getClass().getResource("res/DemoUploader_Uploaded.html")), StringTools.CHARSET_UTF8);

			String htmlFileName = uploadFile.fileName();
			int i = htmlFileName.lastIndexOf('/');
			htmlFileName = htmlFileName.substring(i + 1);
			i = htmlFileName.lastIndexOf('\\');
			htmlFileName = htmlFileName.substring(i + 1);
			htmlFileName = HTTPServer.encodeUrl(htmlFileName, StringTools.CHARSET_UTF8);
			final String f_htmlFileName = htmlFileName;

			html = processTag(html, "_overview", innerHtml -> getOverview(innerHtml, f_htmlFileName, getFileType(uploadFile.fileName())));
			html = html.replace("${FILE-NAME}", uploadFile.fileName());
			html = html.replace("${FILE-SIZE}", "" + uploadFile.data.length);
			html = html.replace("${SUPPLEMENT}", new String(supplement.data, StringTools.CHARSET_UTF8));
			html = html.replace("${FILE-DATA-HEX}", toHex(uploadFile.data));

			_lastUploadedFileData = uploadFile.data;
			_lastUploadedFile = uploadFile.fileName();

			return new HttResHtml(html);
		}

		return new HttResHtml(
				new String(FileTools.readToEnd(this.getClass().getResource("res/DemoUploader_Main.html")), StringTools.CHARSET_UTF8)
				);
	}

	public static class MultiPartFormData {
		private List<MultiPartContent> _contents = new ArrayList<MultiPartContent>();

		public void load(byte[] body) throws Exception {
			int index = findPtn(body, new byte[] { 0x0d, 0x0a }); // find CR-LF

			if(index == -1) {
				throw new Exception();
			}
			byte[] boundary = Arrays.copyOf(body, index);
			index += 2; // skip CR-LF

			for(; ; ) {
				int next = findPtn(body, boundary, index);

				if(next == -1) {
					throw new Exception();
				}
				byte[] contentBody = Arrays.copyOfRange(body, index, next);

				MultiPartContent content = new MultiPartContent();
				content.load(contentBody);
				_contents.add(content);

				index = next + boundary.length;

				/*
				 * boundary + CR-LF  -->  continue
				 * boundary + "--"   -->  end
				 *
				 */
				if(body[index] == 0x2d) {
					break;
				}
				index += 2; // skip CR-LF
			}
		}

		public MultiPartContent get(String name) {
			for(MultiPartContent content : _contents) {
				if(name.equals(content.name())) {
					return content;
				}
			}
			return null;
		}
	}

	public static class MultiPartContent {
		private Map<String, String> _extensions = MapTools.<String>createIgnoreCase();
		public byte[] data;

		public void load(byte[] body) throws Exception {
			int index = findPtn(body, new byte[] { 0x0d, 0x0a }); // find CR-LF

			if(index == -1) {
				throw new Exception();
			}

			// HACK This code is inadequate

			String line = new String(Arrays.copyOf(body, index), StringTools.CHARSET_UTF8); // maybe Content-Disposition:

			for(String entry : line.split("[:]", 2)[1].split("[;]")) {
				String[] tokens = entry.split("[=]", 2);

				if(tokens.length == 2) {
					String key = tokens[0].trim();
					String value = tokens[1].trim();

					if(value.startsWith("\"")) {
						value = value.substring(1);
					}
					if(value.endsWith("\"")) {
						value = value.substring(0, value.length() - 1);
					}
					_extensions.put(key, value);
				}
			}
			index += 2; // skip CR-LF

			while(body[index] != 0x0d) {
				index = findPtn(body, new byte[] { 0x0d, 0x0a }, index); // find CR-LF

				if(index == -1) {
					throw new Exception();
				}
				index += 2; // skip CR-LF
			}
			index += 2; // skip CR-LF

			data = Arrays.copyOfRange(body, index, body.length - 2); // cut CR-LF
		}

		public String name() {
			return _extensions.get("name");
		}

		public String fileName() {
			return _extensions.get("filename");
		}
	}

	private static int findPtn(byte[] data, byte[] ptn) {
		return findPtn(data, ptn, 0);
	}

	private static int findPtn(byte[] data, byte[] ptn, int fromIndex) {

		// HACK This code is inefficient

		for(int index = fromIndex; index + ptn.length <= data.length; index++) {
			if(equals(data, index, ptn, 0, ptn.length)) {
				return index;
			}
		}
		return -1;
	}

	private static boolean equals(byte[] a, int ai, byte[] b, int bi, int length) {
		if(a.length < ai + length) {
			return false;
		}
		if(b.length < bi + length) {
			return false;
		}
		for(int index = 0; index < length; index++) {
			if(a[ai + index] != b[bi + index]) {
				return false;
			}
		}
		return true;
	}

	private String processTag(String html, String tagName, Function<String, String> processor) {
		String openTag = String.format("<%s>", tagName);
		String closeTag = String.format("</%s>", tagName);

		StringTools.Enclosed encl = StringTools.getEnclosed(html, openTag, closeTag);
		String innerHtml = encl.innerText;

		innerHtml = processor.apply(innerHtml);

		html = html.substring(0, encl.bgnBgn) + innerHtml + html.substring(encl.endEnd);
		return html;
	}

	private String getOverview(String innerHtml, String fileName, String fileType) {
		String openTag = String.format("<%s>", fileType);
		String closeTag = String.format("</%s>", fileType);

		StringTools.Enclosed encl = StringTools.getEnclosed(innerHtml, openTag, closeTag);
		innerHtml = encl.innerText;

		innerHtml = innerHtml.replace("${FILE-NAME}", fileName);

		return innerHtml;
	}

	private String getFileType(String fileName) {
		String ext = fileName;
		int i = ext.lastIndexOf('.');
		ext = ext.substring(i + 1);
		ext = ext.toLowerCase();

		String contentType = ExtToContentType.getContentType(ext);

		if(contentType.startsWith("image/")) {
			return "_image";
		}
		if(contentType.startsWith("video/")) {
			return "_video";
		}
		if(contentType.startsWith("audio/")) {
			return "_audio";
		}
		return "_default";
	}

	private static String toHex(byte[] data) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < data.length; index++) {
			byte b = data[index];
			int i = b & 0xff;

			if(index == 1000) {
				buff.append("...");
				break;
			}
			buff.append(StringTools.hexadecimal.charAt(i / 16));
			buff.append(StringTools.hexadecimal.charAt(i % 16));
		}
		return buff.toString();
	}
}
