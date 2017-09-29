package teatime.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import charlotte.htt.HttRequest;
import charlotte.htt.HttResponse;
import charlotte.htt.HttService;
import charlotte.tools.StringTools;

public class TeaTimeService implements HttService {
	private Map<String, TTFile> _files = new TreeMap<String, TTFile>(StringTools.compIgnoreCase);
	private Map<String, TTComponent> _components = new TreeMap<String, TTComponent>(StringTools.compIgnoreCase);
	private Map<String, TTDestroy> _destroies = new TreeMap<String, TTDestroy>(StringTools.compIgnoreCase);
	private Map<String, TTMaintenance> _maintenances = new TreeMap<String, TTMaintenance>(StringTools.compIgnoreCase);
	private List<TTRequestAlter> _requestAlters = new ArrayList<TTRequestAlter>();
	private List<TTResponseAlter> _responseAlters = new ArrayList<TTResponseAlter>();

	public TeaTimeService(Package rootPkg, File docRootDir) {
		// TODO
	}

	public boolean alive;

	@Override
	public boolean interlude() throws Exception {
		return alive;
	}

	@Override
	public HttResponse service(HttRequest req) throws Exception {
		throw null; // TODO
	}
}
