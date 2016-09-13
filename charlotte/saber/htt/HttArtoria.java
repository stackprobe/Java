package charlotte.saber.htt;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;
import charlotte.tools.MapTools;

public abstract class HttArtoria implements HttService {
	private boolean _ended = false;

	public void end() {
		_ended = true;
	}

	@Override
	public boolean interlude() throws Exception {
		return _ended == false;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		return null;
	}

	protected abstract Package getRoot(HttSaberRequest req);

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
		res.setReasonPhrase("OK");
		res.setHeaderFields(MapTools.<String>createIgnoreCase());
		res.setBodyFile(null);
		res.setBody(null);

		return res;
	}
}
