package charlotte.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XStruct {
	public static int valueListMax = 10;

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

	public HugeQueue toLines() {
		HugeQueue ret = new HugeQueue();
		toLines("", "Root", ret);
		return ret;
	}

	private void toLines(String indent, String name, HugeQueue dest) {
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
				if(valueListMax < ++c) {
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

	public HugeQueue toXmlLines() {
		HugeQueue ret = new HugeQueue();
		ret.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		toXmlLines("Root", ret);
		return ret;
	}

	private void toXmlLines(String name, HugeQueue dest) {
		dest.add("<" + name + " count=\"" + this.min + ":" + this.max + "\">");

		for(String brName : this.branches.keySet()) {
			this.branches.get(brName).toXmlLines(brName, dest);
		}
		List<String> values4Prn = getValues4Print();

		int c = 0;
		for(String value : values4Prn) {
			if(valueListMax < ++c) {
				dest.add("<has-more-values/>");
				break;
			}
			dest.add("<value>" + value + "</value>");
		}

		dest.add("</" + name + ">");
	}

	public List<String> toPathLines() {
		List<String> ret = new ArrayList<String>();
		toPathLines(null, ret);
		return ret;
	}

	private void toPathLines(String parentPath, List<String> ret) {
		for(String brName : this.branches.keySet()) {
			XStruct br = this.branches.get(brName);
			String path = parentPath == null ? brName : parentPath + "/" + brName;

			ret.add(path);

			br.toPathLines(path, ret);
		}
	}

	public HugeQueue toPathValueLines() {
		HugeQueue ret = new HugeQueue();
		toPathValueLines(null, ret);
		return ret;
	}

	private void toPathValueLines(String parentPath, HugeQueue ret) {
		for(String brName : this.branches.keySet()) {
			XStruct br = this.branches.get(brName);
			String path = parentPath == null ? brName : parentPath + "/" + brName;

			ret.add(path + " " + br.min + ":" + br.max);

			{
				List<String> values4Prn = br.getValues4Print();

				if(1 <= values4Prn.size()) {
					ret.add("{");
					int c = 0;
					for(String value : values4Prn) {
						if(valueListMax < ++c) {
							ret.add("\t...");
							break;
						}
						ret.add("\t" + value);
					}
					ret.add("}");
				}
			}

			br.toPathValueLines(path, ret);
		}
	}

	public List<String> toPathAllValueLines() {
		List<String> ret = new ArrayList<String>();
		toPathAllValueLines(null, ret);
		return ret;
	}

	private void toPathAllValueLines(String parentPath, List<String> ret) {
		for(String brName : this.branches.keySet()) {
			XStruct br = this.branches.get(brName);
			String path = parentPath == null ? brName : parentPath + "/" + brName;

			ret.add(path + " " + br.min + ":" + br.max);
			ret.add("{");

			for(String value : br.getValues4Print()) {
				ret.add("\t" + value);
			}
			ret.add("}");

			br.toPathAllValueLines(path, ret);
		}
	}
}
