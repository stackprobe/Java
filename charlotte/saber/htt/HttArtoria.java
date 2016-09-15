package charlotte.saber.htt;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;
import charlotte.tools.ArrayTools;
import charlotte.tools.ExtToContentType;
import charlotte.tools.FileTools;
import charlotte.tools.MapTools;
import charlotte.tools.ReflecTools;
import charlotte.tools.StringTools;
import charlotte.tools.ValueBox;
import charlotte.tools.ValueSetter;

public abstract class HttArtoria implements HttService, Closeable {
	private static HttArtoria _self;

	public static HttArtoria getInstance() {
		return _self;
	}

	public HttArtoria() {
		_self = this;
	}

	private boolean _ended = false;

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
		Entry entry = getEntry(p);
		String urlPath = urlToUrlPath(hr.getUrlString());
		ValueBox<HttSaber404> saber404Box = new ValueBox<HttSaber404>();
		List<Entry> entries = urlPathToEntries(urlPath, entry, saber404Box);
		HttSaberResponse res;

		if(entries == null) {
			HttSaber404 saber404 = saber404Box.get();

			if(saber404 != null) {
				res = saber404.getResponse(req);

				if(res == null) {
					throw new NullPointerException("saber404.getResponse() returned null");
				}
			}
			else {
				res = getResponse404(req);
			}
		}
		else {
			entry = entries.get(entries.size() - 1);

			if(entry.saber != null) {
				alter(entries, req);
				res = entry.saber.doRequest(req);

				if(res == null) {
					throw new NullPointerException("entry.saber.doRequest() returned null");
				}
				alter(entries, res);
			}
			else if(entry.file != null) {
				alter(entries, req);
				res = download(entry.file);
				alter(entries, res);
			}
			else {
				res = getResponse301(req);
			}
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

	public HttSaberResponse createResponse() throws Exception {
		HttSaberResponse res = new HttSaberResponse();

		res.setHTTPVersion("HTTP/1.1");
		res.setStatusCode(200);
		res.setReasonPhrase("Saber Lion");
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

	private Map<String, Entry> _entries = MapTools.<Entry>createIgnoreCase();

	public Entry getEntry(Package p) throws Exception {
		Entry entry = _entries.get(p.getName());

		if(entry == null) {
			entry = createEntry(p);
			_entries.put(p.getName(), entry);
		}
		return entry;
	}

	public Class<?> getCritClassObj() {
		return HttArtoria.class;
	}

	public static class Entry {
		public String classPath;
		public String path;
		public Map<String, Entry> children;
		public List<Entry> alters;
		public Entry saber404Entry;
		public HttSaber saber;
		public HttSaberAlter alter;
		public HttSaber404 saber404;
		public File file;
	}

	public Entry createEntry(Package p) throws Exception {
		String dir = ReflecTools.getDir(getCritClassObj(), p);

		if(FileTools.isDirectory(dir) == false) {
			throw new RuntimeException("プロジェクトに含まれないパッケージは取得出来ません。[" + dir + "]");
		}
		Entry ret = new Entry();

		ret.classPath = p.getName();
		ret.path = dir;
		ret.children = MapTools.<Entry>createIgnoreCase();
		ret.alters = new ArrayList<Entry>();

		List<String> paths = FileTools.ls(dir);

		ArrayTools.sort(paths, StringTools.compIgnoreCase);

		for(String path : paths) {
			String lPath = FileTools.getLocal(path);
			String lClassPath = FileTools.eraseExt(lPath);
			String classPath = ret.classPath + "." + lClassPath;
			Entry entry = createEntry(classPath, path);

			if(entry == null) {
				// noop
			}
			else if(entry.saber != null) {
				lPath = lPathToSaberLPath(lPath);
				ret.children.put(lPath, entry);
			}
			else if(entry.alter != null) {
				ret.alters.add(entry);
			}
			else if(entry.saber404 != null) {
				ret.saber404Entry = entry;
			}
			else {
				ret.children.put(lPath, entry);
			}
		}
		return ret;
	}

	public Entry createEntry(String classPath, String path) throws Exception {
		if(isIgnoreLocalPath(FileTools.getLocal(path))) {
			return null;
		}
		if(FileTools.isDirectory(path)) {
			return createEntry(Package.getPackage(classPath));
		}
		Entry ret;

		if(StringTools.endsWithIgnoreCase(path, ".class")) {
			Class<?> classObj = Class.forName(classPath);

			if(ReflecTools.typeOf(classObj, HttSaber.class)) {
				ret = new Entry();
				ret.saber = (HttSaber)ReflecTools.invokeDeclaredCtor(classObj, new Object[0]);
			}
			else if(ReflecTools.typeOf(classObj, HttSaberAlter.class)) {
				ret = new Entry();
				ret.alter = (HttSaberAlter)ReflecTools.invokeDeclaredCtor(classObj, new Object[0]);
			}
			else if(ReflecTools.typeOf(classObj, HttSaber404.class)) {
				ret = new Entry();
				ret.saber404 = (HttSaber404)ReflecTools.invokeDeclaredCtor(classObj, new Object[0]);
			}
			else {
				return null;
			}
		}
		else {
			ret = new Entry();
			ret.file = new File(path);
		}
		ret.classPath = classPath;
		ret.path = path;

		return ret;
	}

	public boolean isIgnoreLocalPath(String lPath) {
		return "res".equalsIgnoreCase(lPath) ||
				"resource".equalsIgnoreCase(lPath) ||
				"template".equalsIgnoreCase(lPath);
	}

	private static final String WILDCARD_SUFFIX = ".*";

	public String lPathToSaberLPath(String lPath) {
		lPath = FileTools.eraseExt(lPath);
		lPath += WILDCARD_SUFFIX;

		return lPath;
	}

	@Override
	public void close() throws IOException {
		clear();
	}

	public void clear() {
		clearAllEntry();
	}

	public void clearAllEntry() {
		for(Entry entry : _entries.values()) {
			clearEntry(entry);
		}
		_entries.clear();
	}

	public void clearEntry(Entry entry) {
		if(entry.children != null) {
			for(Entry e : entry.children.values()) {
				clearEntry(e);
			}
		}
		if(entry.alters != null) {
			for(Entry e : entry.alters) {
				clearEntry(e);
			}
		}
		FileTools.close(entry.saber);
		entry.saber = null;
		FileTools.close(entry.alter);
		entry.alter = null;
		FileTools.close(entry.saber404);
		entry.saber404 = null;
	}

	public String urlToUrlPath(String url) {
		int index;

		index = url.indexOf('?');
		if(index != -1) {
			url = url.substring(0, index);
		}

		index = url.indexOf("://");
		if(index != -1) {
			url = url.substring(index + 3);

			index = url.indexOf('/');
			if(index != -1) {
				url = url.substring(index);
			}
			else {
				url = "/";
			}
		}
		if(StringTools.endsWith(url, "/")) {
			url += getIndexHtml();
		}
		url = String.join(
				"/",
				StringTools.tokenize(url, "/", false, true)
				);

		return url;
	}

	public String getIndexHtml() {
		//return "index.htm";
		return "index.html";
	}

	public List<Entry> urlPathToEntries(String urlPath, Entry entry, ValueSetter<HttSaber404> saber404Box) {
		List<String> lPaths = StringTools.tokenize(urlPath, "/");
		List<Entry> entries = new ArrayList<Entry>();

		entries.add(entry);

		for(String lPath : lPaths) {
			if(entry.children == null) {
				return null;
			}
			if(entry.saber404Entry != null) {
				saber404Box.set(entry.saber404Entry.saber404);
			}
			Entry entryNew = entry.children.get(lPath);

			if(entryNew == null) {
				lPath = lPathToSaberLPath(lPath);
				entryNew = entry.children.get(lPath);

				if(entryNew == null) {
					return null;
				}
			}
			entries.add(entryNew);
			entry = entryNew;
		}
		return entries;
	}

	public HttSaberResponse getResponse404(HttSaberRequest req) throws Exception {
		HttSaber saber = getSaber404(req);

		if(saber == null) {
			throw new NullPointerException("getSaber404() returned null");
		}
		HttSaberResponse res = saber.doRequest(req);

		if(res == null) {
			throw new NullPointerException("getSaber404().doRequest() returned null");
		}
		return res;
	}

	private HttSaber _saber404;

	public HttSaber getSaber404(HttSaberRequest req) {
		if(_saber404 == null) {
			_saber404 = new HttSaber() {
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
		return _saber404;
	}

	public HttSaberResponse getResponse301(HttSaberRequest req) throws Exception {
		String urlString = req.getUrlString();
		int index;

		index = urlString.indexOf('?');
		if(index != -1) {
			urlString = urlString.substring(0, index);
		}

		if(StringTools.endsWith(urlString, "/")) {
			return getResponse404(req);
		}
		String location = urlString + "/";

		HttSaberResponse res = createResponse();

		res.setStatusCode(301);
		res.getHeaderFields().put("Location", location);

		return res;
	}

	public HttSaberResponse download(File file) throws Exception {
		HttSaberResponse res = createResponse();

		res.setBodyFile(file);
		res.getHeaderFields().put("Content-Type", getContentType(file));

		return res;
	}

	public String getContentType(File file) throws Exception {
		return ExtToContentType.getContentType(FileTools.getExt(file.getCanonicalPath()));
	}

	public void alter(List<Entry> entries, HttSaberRequest req) throws Exception {
		for(int index = 0; index < entries.size(); index++) {
			Entry entry = entries.get(index);

			for(int altndx = 0; altndx < entry.alters.size(); altndx++) {
				entry.alters.get(altndx).alter.alter(req);
			}
		}
	}

	public void alter(List<Entry> entries, HttSaberResponse res) throws Exception {
		for(int index = entries.size() - 1; 0 <= index; index--) {
			Entry entry = entries.get(index);

			for(int altndx = entry.alters.size(); 0 <= altndx; altndx--) {
				entry.alters.get(altndx).alter.alter(res);
			}
		}
	}
}
