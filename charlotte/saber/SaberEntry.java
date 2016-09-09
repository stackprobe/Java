package charlotte.saber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charlotte.saber.htt.HttSaberAlter;
import charlotte.tools.MapTools;

public abstract class SaberEntry {
	public String packageString;
	public String path;
	public Map<String, SaberEntry> children = MapTools.<SaberEntry>createIgnoreCase();
	public List<HttSaberAlter> alters = new ArrayList<HttSaberAlter>();
}
