package charlotte_test.htt.test_server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttServer;
import charlotte.htt.HttService;
import charlotte.htt.response.HttResFileImage;
import charlotte.htt.response.HttResHtml;
import charlotte.tools.FileTools;
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

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		if(req.getUrl().getPath().equals("/uploaded-file.bin")) {
			return new HttResFileImage(_lastUploadedFileData, "$.bin");
		}

		if(req.getUrl().getPath().equals("/upload")) {
			byte[] body = req.getBodyPart();

			MultiPartFormData mpfd = new MultiPartFormData();
			mpfd.load(body);

			MultiPartContent uploadFile = mpfd.get("upload-file");
			MultiPartContent supplement = mpfd.get("supplement");

			String html = new String(FileTools.readToEnd(this.getClass().getResource("res/DemoUploader_Uploaded.html")), StringTools.CHARSET_UTF8);

			html = html.replace("${OVERVIEW}", getOverview(getFileType(uploadFile.data)));
			html = html.replace("${FILE-NAME}", uploadFile.fileName());
			html = html.replace("${FILE-SIZE}", "" + uploadFile.data.length);
			html = html.replace("${SUPPLEMENT}", new String(supplement.data, StringTools.CHARSET_UTF8));
			html = html.replace("${FILE-DATA-HEX}", toHex(uploadFile.data));

			_lastUploadedFileData = uploadFile.data;

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

			// FIXME This code is inadequate

			String line = new String(Arrays.copyOf(body, index)); // maybe Content-Disposition:

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

		// FIXME This code is inefficient

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

	private String getOverview(String fileType) {
		if("image".equals(fileType)) {
			return "<img src='/uploaded-file.bin'/>";
		}
		if("movie".equals(fileType)) {
			// TODO
		}
		return "<a href='/uploaded-file.bin'>Download</a>";
	}

	private String getFileType(byte[] data) throws Exception {
		if(equals(data, 0, "BM".getBytes(StringTools.CHARSET_ASCII), 0, 2)) { // ? bmp
			return "image";
		}
		if(equals(data, 0, "GIF87a".getBytes(StringTools.CHARSET_ASCII), 0, 6)) { // ? gif(1)
			return "image";
		}
		if(equals(data, 0, "GIF89a".getBytes(StringTools.CHARSET_ASCII), 0, 6)) { // ? gif(2)
			return "image";
		}
		if(equals(data, 0, StringTools.hex("ffd8"), 0, 2)) { // ? jpeg
			return "image";
		}
		if(equals(data, 0, StringTools.hex("89504e470d0a1a0a"), 0, 8)) { // ? png
			return "image";
		}

		// TODO

		return null;
	}

	private static String toHex(byte[] data) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < data.length; index++) {
			byte b = data[index];
			int i = b & 0xff;

			if(index % 40 == 0 && index != 0) {
				buff.append("<br/>");
			}
			buff.append(StringTools.hexadecimal.charAt(i / 16));
			buff.append(StringTools.hexadecimal.charAt(i % 16));
		}
		return buff.toString();
	}
}
