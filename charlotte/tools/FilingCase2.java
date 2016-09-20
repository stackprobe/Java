package charlotte.tools;

import java.util.List;
import java.util.Map;

public class FilingCase2 {
	private String _rootDir;

	public FilingCase2(String rootDir) {
		_rootDir = rootDir;
	}

	private void init() {
		throw null; // TODO
	}

	public List<String> getTables() {
		throw null; // TODO
	}

	public Map<String, String> get(String table, String column, String value) {
		throw null; // TODO
	}

	public void add(String table, Map<String, String> record) {
		throw null; // TODO
	}

	public void scan(String table, AcceptListener<Map<String, String>> scanner) {
		throw null; // TODO
	}
}
