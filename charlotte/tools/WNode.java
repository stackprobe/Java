package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class WNode {
	public String name;
	public String value;
	public List<WNode> children = new ArrayList<WNode>();

	public static WNode create(XNode xNode) {
		WNode wNode = new WNode();

		wNode.name = xNode.getName();
		wNode.value = xNode.getValue();

		for(XNode xChild : xNode.getChildren()) {
			wNode.children.add(create(xChild));
		}
		return wNode;
	}

	public XNode getXNode() {
		XNode xNode = new XNode(name, value);

		for(WNode wChild : children) {
			xNode.getChildren().add(wChild.getXNode());
		}
		return xNode;
	}

	public WNode get(String name) {
		int index = getIndex(name);

		if(index == -1) {
			return null;
		}
		return children.get(index);
	}

	public int getIndex(String name) {
		for(int index = 0; index < children.size(); index++) {
			if(name.equals(children.get(index).name)) {
				return index;
			}
		}
		return -1;
	}

	public void remove(String name) {
		for(int index = children.size() - 1; 0 <= index; index--) {
			if(name.equals(children.get(index).name)) {
				children.remove(index);
			}
		}
	}

	public void hissunize(String path) {
		hissunize(StringTools.tokenize(path, XNode.PATH_DLMTRS), 0);
	}

	private void hissunize(List<String> pathTokens, int ptIndex) {
		{
			WNode child = get(pathTokens.get(ptIndex));

			if(child == null) {
				child = new WNode();
				child.name = pathTokens.get(ptIndex);
				children.add(child);
			}
		}

		if(ptIndex + 1 < pathTokens.size()) {
			for(WNode child : children) {
				child.hissunize(pathTokens, ptIndex + 1);
			}
		}
	}
}
