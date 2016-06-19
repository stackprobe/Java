package charlotte.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XStruct {
	public int min = 1;
	public int max = 1;
	public Set<String> values = SetTools.create();
	public Map<String, XStruct> branches = MapTools.<XStruct>create();

	public void add(XNode root) {
		boolean firstTime = this.values.size() == 0;

		for(XNode child : root.getChildren()) {
			XStruct branch = this.branches.get(child.getName());

			if(branch == null) {
				branch = new XStruct();
				branch.min = firstTime ? Integer.MAX_VALUE : 0;
				this.branches.put(child.getName(), branch);
			}
			branch.add(child);
		}
		for(String name : this.branches.keySet()) {
			XStruct branch = this.branches.get(name);
			int count = getCount(root.getChildren(), name);

			branch.min = Math.min(branch.min, count);
			branch.max = Math.max(branch.max, count);
		}
		this.values.add(root.getValue());
	}

	private int getCount(List<XNode> nodes, String name) {
		int count = 0;

		for(XNode node : nodes) {
			if(node.getName().equals(name)) {
				count++;
			}
		}
		return count;
	}

	public List<String> toLines() {
		List<String> ret = new ArrayList<String>();
		toLines("", "Root", ret);
		return ret;
	}

	private void toLines(String indent, String name, List<String> dest) {
		dest.add(indent + name + " " + this.min + ":" + this.max);

		if(1 <= this.branches.size()) {
			dest.add(indent + "{");

			for(String brName : this.branches.keySet()) {
				this.branches.get(brName).toLines(indent + "\t", brName, dest);
			}
			dest.add(indent + "}");
		}
		List<String> values4Prn = getValues4Print();

		if(1 <= values4Prn.size()) {
			dest.add(indent + "<");

			int c = 0;
			for(String value : values4Prn) {
				if(10 < ++c) {
					dest.add(indent + "\t...");
					break;
				}
				dest.add(indent + "\t" + value);
			}
			dest.add(indent + ">");
		}
	}

	private List<String> getValues4Print() {
		List<String> ret = new ArrayList<String>();

		for(String value : this.values) {
			if(value.length() != 0) {
				ret.add(value);
			}
		}
		return ret;
	}

	public List<String> toXmlLines() {
		List<String> ret = new ArrayList<String>();
		ret.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		toXmlLines("Root", ret);
		return ret;
	}

	private void toXmlLines(String name, List<String> dest) {
		dest.add("<" + name + " count=\"" + this.min + ":" + this.max + "\">");

		for(String brName : this.branches.keySet()) {
			this.branches.get(brName).toXmlLines(brName, dest);
		}
		List<String> values4Prn = getValues4Print();

		int c = 0;
		for(String value : values4Prn) {
			if(10 < ++c) {
				dest.add("<has-more-values/>");
				break;
			}
			dest.add("<value>" + value + "</value>");
		}

		dest.add("</" + name + ">");
	}
}