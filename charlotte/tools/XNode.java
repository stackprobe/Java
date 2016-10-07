package charlotte.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XNode {
	private String _name;
	private String _value;
	private List<XNode> _children;

	public XNode() {
		this(null);
	}

	public XNode(String name) {
		this(name, null);
	}

	public XNode(String name, String value) {
		this(name, value, null);
	}

	public XNode(String name, String value, List<XNode> children) {
		if(name == null) {
			name = "";
		}
		if(value == null) {
			value = "";
		}
		if(children == null) {
			children = new ArrayList<XNode>();
		}
		_name = name;
		_value = value;
		_children = children;
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	public List<XNode> getChildren() {
		return _children;
	}

	public static XNode load(byte[] fileData) throws Exception {
		WorkDir wd = new WorkDir();
		try {
			String file = wd.makeSubPath();
			FileTools.writeAllBytes(file, fileData);
			return load(file);
		}
		finally {
			FileTools.close(wd);
		}
	}

	public static XNode load(String file) throws Exception {
		XNode root = load(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(file)));
		root = root._children.get(0);
		postLoad(root);
		return root;
	}

	private static XNode load(Node element) {
		NamedNodeMap attrs = element.getAttributes();
		List<XNode> children = new ArrayList<XNode>();

		if(attrs != null) {
			for(int i = 0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);

				children.add(new XNode(
						attr.getNodeName(),
						attr.getNodeValue()
						));
			}
		}
		NodeList childNodes = element.getChildNodes();
		StringBuffer buff = new StringBuffer();

		for(int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);

			switch(childNode.getNodeType()) {
			case Node.TEXT_NODE:
				buff.append(childNode.getNodeValue());
				break;

			case Node.ELEMENT_NODE:
				children.add(load(childNode));
				break;
			}
		}
		return new XNode(
				element.getNodeName(),
				buff.toString(),
				children
				);
	}

	//public static final String TAG_NAME_NAMESPACE = "_namespace";

	private static void postLoad(XNode node) {
		node._name = trimValue(node._name);
		node._value = trimValue(node._value);

		{
			int clnPos = node._name.indexOf(':');

			if(clnPos != -1) {
				//String namespace = node._name.substring(0, clnPos);

				node._name = node._name.substring(clnPos + 1);
				//node._children.add(new XNode(TAG_NAME_NAMESPACE, namespace));
			}
		}

		for(XNode child : node._children) {
			postLoad(child);
		}
	}

	private static String trimValue(String value) {
		value = value.replace("　", StringTools.S_ESCAPE); // XXX
		value = value.trim();
		value = value.replace(StringTools.S_ESCAPE, "　"); // XXX

		return value;
	}

	private static final String SAVE_INDENT = "\t";
	private static final String SAVE_NEW_LINE = "\n";

	public void save(String file) throws Exception {
		FileTools.writeAllBytes(file, getBytes());
	}

	public byte[] getBytes() throws Exception {
		List<String> lines = new ArrayList<String>();
		lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		addTo(lines, "");
		String text = StringTools.join(SAVE_NEW_LINE, lines);
		return text.getBytes(StringTools.CHARSET_UTF8);
	}

	private void addTo(List<String> lines, String indent) {
		String name = _name;
		String value = _value;

		//name = name.replace(":", "_COLON_");

		if(name.length() == 0) {
			name = "node";
		}
		//value = value.trim(); // moved -> postLoad()

		if(_children.size() != 0) {
			lines.add(indent + "<" + name + ">");

			for(XNode child : _children) {
				child.addTo(lines, indent + SAVE_INDENT);
			}
			if(value.length() != 0) {
				lines.add(indent + SAVE_INDENT + value);
			}
			lines.add(indent + "</" + name + ">");
		}
		else if(_value.length() != 0) {
			lines.add(indent + "<" + name + ">" + value + "</" + name + ">");
		}
		else {
			lines.add(indent + "<" + name + "/>");
		}
	}

	private static final String PATH_DLMTRS = "/\\";

	public List<XNode> getNodes(String path) {
		List<XNode> ret = new ArrayList<XNode>();
		collectNodes(StringTools.tokenize(path, PATH_DLMTRS, false, true), 0, ret);
		return ret;
	}

	private void collectNodes(List<String> pathTokens, int ptIndex, List<XNode> dest) {
		if(ptIndex < pathTokens.size()) {
			for(XNode child : _children) {
				if(child.getName().equals(pathTokens.get(ptIndex))) {
					child.collectNodes(pathTokens, ptIndex + 1, dest);
				}
			}
		}
		else {
			dest.add(this);
		}
	}

	public XNode getNode(String path) {
		List<XNode> ret = getNodes(path);

		if(ret.size() == 0) {
			System.out.println("パス " + path + " のタグが見つかりません。");
			return null;
		}
		return ret.get(0);
	}

	public XNode getNode(String path, int index) {
		List<XNode> ret = getNodes(path);

		if(ret.size() <= index) {
			System.out.println("パス " + path + " の " + (index + 1) + " 番目のタグが見つかりません。");
			return null;
		}
		return ret.get(index);
	}

	public String getNodeValue(String path) {
		XNode node = getNode(path);

		if(node == null) {
			return null;
		}
		return node.getValue();
	}

	public String getNodeValue(String path, int index) {
		XNode node = getNode(path, index);

		if(node == null) {
			return null;
		}
		return node.getValue();
	}
}
