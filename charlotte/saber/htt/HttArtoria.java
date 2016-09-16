package charlotte.saber.htt;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;
import charlotte.tools.ExtToContentType;
import charlotte.tools.FileTools;
import charlotte.tools.MapTools;
import charlotte.tools.ReflecTools;
import charlotte.tools.SetTools;
import charlotte.tools.StringTools;

public abstract class HttArtoria implements HttService, Closeable {
	private static HttArtoria _self;

	public static HttArtoria getInstance() {
		return _self;
	}

	public HttArtoria() {
		_self = this;
	}

	private boolean _ended = false;

	/**
	 * thread safe
	 */
	public void end() {
		_ended = true;
	}

	@Override
	public boolean interlude() throws Exception {
		return _ended == false;
	}

	@Override
	public HttResponse service(HttRequest hr) throws Exception {
		HttSaberRequest req = createRequest(hr);
		Package p = getRoot(req);

		if(p == null) {
			throw new NullPointerException("getRoot() returned null");
		}
		Root root = getRoot(p);
		String urlPath = urlToUrlPath(hr.getUrlString());
		Entry entry = root.entries.get(urlPath);

		if(entry == null) {
			entry = root.entries.get(urlPathToSaberUrlPath(urlPath));
		}
		HttSaberResponse res;

		if(entry == null) {
			res = getResponse301(root, urlPath, req);

			if(res == null) {
				res = root.alter.doRequest(req);
			}
		}
		else if(entry.saber == null) {
			res = download(entry.file);
		}
		else {
			res = entry.saber.doRequest(req);
		}
		return getHttResponse(res);
	}

	protected abstract Package getRoot(HttSaberRequest req) throws Exception;

	public HttSaberRequest createRequest() {
		HttSaberRequest req = new HttSaberRequest();

		req.setMethod("GET");
		req.setUrlString("http://localhost/");
		req.setHTTPVersion("HTTP/1.1");
		req.setHeaderFields(MapTools.<String>createIgnoreCase());
		req.setBody(new byte[0]);

		return req;
	}

	public HttSaberRequest createRequest(HttRequest hr) {
		HttSaberRequest req = new HttSaberRequest();

		req.setMethod(hr.getMethod());
		req.setUrlString(hr.getUrlString());
		req.setHTTPVersion(hr.getHTTPVersion());
		req.setHeaderFields(hr.getHeaderFields());
		req.setBody(hr.getBodyPart());

		return req;
	}

	public HttSaberResponse createResponse() {
		HttSaberResponse res = new HttSaberResponse();

		res.setHTTPVersion("HTTP/1.1");
		res.setStatusCode(200);
		res.setReasonPhrase("Gaooooooo");
		res.setHeaderFields(MapTools.<String>createIgnoreCase());
		res.setBodyFile(null);
		res.setBody(null);

		return res;
	}

	public HttResponse getHttResponse(final HttSaberResponse res) {
		return new HttResponse() {
			@Override
			public String getHTTPVersion() throws Exception {
				return res.getHTTPVersion();
			}

			@Override
			public int getStatusCode() throws Exception {
				return res.getStatusCode();
			}

			@Override
			public String getReasonPhrase() throws Exception {
				return res.getReasonPhrase();
			}

			@Override
			public void writeHeaderFields(Map<String, String> dest) throws Exception {
				dest.putAll(res.getHeaderFields());
			}

			@Override
			public File getBodyPartFile() throws Exception {
				return res.getBodyFile();
			}

			@Override
			public byte[] getBodyPart() throws Exception {
				return res.getBody();
			}
		};
	}

	private Map<String, Root> _roots = MapTools.<Root>createIgnoreCase();

	public Root getRoot(Package p) throws Exception {
		Root root = _roots.get(p.getName());

		if(root == null) {
			root = createRoot(p);
			_roots.put(p.getName(), root);
		}
		return root;
	}

	public static class Root {
		public Map<String, Entry> entries;
		public HttSaberAlter alter;
	}

	public static class Entry {
		public HttSaber saber;
		public File file;
	}

	public Root createRoot(Package p) throws Exception {
		String dir = ReflecTools.getDir(getCritClassObj(), p);

		if(FileTools.isDirectory(dir) == false) {
			throw new RuntimeException("プロジェクトに含まれないパッケージは取得出来ません。");
		}
		Root root = new Root();

		root.entries = MapTools.<Entry>createIgnoreCase();

		for(String path : FileTools.lss(dir)) {
			path = FileTools.norm(path);

			if(FileTools.isDirectory(path)) {
				continue;
			}
			if(isIgnorePath(path, dir)) {
				continue;
			}
			String urlPath = getUrlPath(path, dir);

			if(StringTools.endsWithIgnoreCase(path, ".class")) {
				String className = getClassName(path);
				Class<?> classObj = Class.forName(className);

				if(ReflecTools.typeOf(classObj, HttSaber.class)) {
					HttSaber saber = (HttSaber)ReflecTools.invokeDeclaredCtor(classObj, new Object[0]);

					Entry entry = new Entry();
					entry.saber = saber;
					root.entries.put(urlPathToSaberUrlPath(urlPath), entry);

					if(saber instanceof HttSaberAlter) {
						root.alter = (HttSaberAlter)saber;
					}
				}
			}
			else {
				Entry entry = new Entry();
				entry.file = new File(path);
				root.entries.put(urlPath, entry);
			}
		}
		if(root.alter == null) {
			root.alter = getAlter();
		}
		return root;
	}

	public Class<?> getCritClassObj() {
		return HttArtoria.class;
	}

	public boolean isIgnorePath(String path, String rootDir) {
		String ext = FileTools.getExt(path);

		if(ext.equalsIgnoreCase("html")) {
			String tmp = path;

			tmp = FileTools.eraseExt(tmp);
			tmp += ".class";

			if(FileTools.exists(tmp)) {
				return true;
			}
		}
		path = path.substring(rootDir.length());

		Set<String> lps = SetTools.createIgnoreCase();
		lps.addAll(StringTools.tokenize(path, "/"));

		return lps.contains("res") ||
				 lps.contains("resource") ||
				 lps.contains("template") ||
				 lps.contains("temp") ||
				 lps.contains("tmp");
	}

	public String getUrlPath(String path, String rootDir) {
		String ret = path;

		ret = ret.substring(rootDir.length());
		ret = String.join(
				"/",
				StringTools.tokenize(ret, "/", false, true)
				);

		return ret;
	}

	public String getClassName(String path) {
		String ret = path;

		ret = ret.substring(getBinDirLen());
		ret = FileTools.eraseExt(ret);
		ret = String.join(
				".",
				StringTools.tokenize(ret, "/", false, true)
				);

		return ret;
	}

	private int _binDirLen = -1;

	public int getBinDirLen() {
		if(_binDirLen == -1) {
			_binDirLen = ReflecTools.getBinDir(getCritClassObj()).length();
		}
		return _binDirLen;
	}

	public String urlPathToSaberUrlPath(String urlPath) {
		String ret = urlPath;

		ret = FileTools.eraseExt(ret);
		ret += ".*";

		return ret;
	}

	public String urlToUrlPath(String urlString) {
		String ret = urlString;
		int index;

		index = ret.indexOf('?');
		if(index != -1) {
			ret = ret.substring(0, index);
		}

		index = ret.indexOf("://");
		if(index != -1) {
			index = ret.indexOf('/', index + 3);
			if(index != -1) {
				ret = ret.substring(index);
			}
		}

		if(ret.length() == 0) {
			return getIndexHtml();
		}
		if(ret.charAt(ret.length() - 1) == '/') {
			ret += getIndexHtml();
		}
		ret = String.join(
				"/",
				StringTools.tokenize(ret, "/", false, true)
				);

		return ret;
	}

	public String getIndexHtml() {
		return "index.html";
	}

	public HttSaberResponse download(File file) throws Exception {
		HttSaberResponse res = createResponse();

		res.getHeaderFields().put("Content-Type", getContentType(file));
		res.setBodyFile(file);

		return res;
	}

	public String getContentType(File file) throws Exception {
		return ExtToContentType.getContentType(FileTools.getExt(file.getCanonicalPath()));
	}

	public HttSaberAlter getAlter() {
		return new HttSaberAlter() {
			@Override
			public HttSaberResponse doRequest(HttSaberRequest req) throws Exception {
				HttSaberResponse res = createResponse();
				res.setStatusCode(404);
				return res;
			}

			@Override
			public void close() throws IOException {
				// noop
			}
		};
	}

	public HttSaberResponse getResponse301(Root root, String urlPath, HttSaberRequest req) {
		urlPath += "/" + getIndexHtml();

		if(root.entries.containsKey(urlPath) == false) {
			return null;
		}
		String host = req.getHeaderFields().get("Host");

		if(host == null) {
			return null;
		}
		String location = "http://" + host + "/" + urlPath;

		HttSaberResponse res = createResponse();

		res.setStatusCode(301);
		res.getHeaderFields().put("Location", location);

		return res;
	}

	@Override
	public void close() throws IOException {
		clear();
	}

	public void clear() {
		clearAllRoot();
	}

	public void clearAllRoot() {
		for(Root root : _roots.values()) {
			clearRoot(root);
		}
		_roots.clear();
	}

	private void clearRoot(Root root) {
		for(Entry entry : root.entries.values()) {
			FileTools.close(entry.saber);
		}
		//FileTools.close(root.alter); // dont!
	}
}
