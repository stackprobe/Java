package teatime.server;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;
import charlotte.tools.StringTools;

public class TeaTimeService implements HttService {
	private Map<String, TTFile> _files = new TreeMap<String, TTFile>(StringTools.compIgnoreCase);
	private Map<String, TTComponent> _components = new TreeMap<String, TTComponent>(StringTools.compIgnoreCase);

	public TeaTimeService(Package rootPkg, File docRootDir) {
		// TODO
	}

	public boolean dead;

	@Override
	public boolean interlude() throws Exception {
		return !dead;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		throw null; // TODO
	}
}
