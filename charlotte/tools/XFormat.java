package charlotte.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XFormat {
	private Map<String, XFormat> _children = MapTools.<XFormat>create();
	private int _min = 1;
	private int _max = 1;
	private boolean _mad = false;

	public XFormat(XNode root) throws Exception {
		for(XNode node : root.getChildren()) {
			String name = node.getName();

			if(name.equals("num")) {
				String value = node.getValue();
				int index;

				index = value.indexOf(':');
				if(index == -1) {
					throw new IllegalArgumentException();
				}

				String v1 = value.substring(0, index);
				String v2 = value.substring(index + 1);

				_min = Integer.parseInt(v1);

				if(v2.equals("*")) {
					_max = IntTools.IMAX;
				}
				else {
					_max = Integer.parseInt(v2);
				}

				if(_min < 0 || _max < _min || IntTools.IMAX < _max) {
					throw new IllegalArgumentException();
				}
			}
			else if(name.equals("mad")) {
				_mad = true;
			}
			else {
				_children.put(name, new XFormat(node));
			}
		}
	}

	public void check(XNode root) throws Exception {
		check(root, "Root");
	}

	private void check(XNode root, String debugPath) throws Exception {
		Map<String, List<XNode>> m = MapTools.<List<XNode>>create();

		for(XNode node : root.getChildren()) {
			String name = node.getName();
			List<XNode> nodes = m.get(name);

			if(nodes == null) {
				nodes = new ArrayList<XNode>();
				m.put(name, nodes);
			}
			nodes.add(node);
		}
		if(_mad == false) {
			for(String name : m.keySet()) {
				if(_children.containsKey(name) == false) {
					throw new IllegalXFormatException(
							name,
							debugPath,
							"存在してはならないタグです。"
							);
				}
			}
		}
		for(String name : _children.keySet()) {
			XFormat format = _children.get(name);
			List<XNode> nodes = m.get(name);
			int num;

			if(nodes == null) {
				num = 0;
			}
			else {
				num = nodes.size();
			}
			String message = null;

			if(num < _min) {
				message = "タグが少なすぎます。";
			}
			else if(_max < num) {
				message = "タグが多すぎます。";
			}
			if(message != null) {
				throw new IllegalXFormatException(
						name,
						debugPath,
						message + num + " " + _min + ":" + _max
						);
			}
		}
		for(String name : _children.keySet()) {
			XFormat format = _children.get(name);
			List<XNode> nodes = m.get(name);

			if(nodes != null) {
				for(XNode node : nodes) {
					format.check(node, debugPath + "/" + name);
				}
			}
		}
	}

	public static class IllegalXFormatException extends Exception {
		public IllegalXFormatException(String name, String debugPath, String reason) {
			super(debugPath + "/" + name + " -> " + reason);
		}
	}
}
