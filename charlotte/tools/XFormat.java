package charlotte.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XFormat {
	private Map<String, XFormat> _children = MapTools.<XFormat>create();
	private int _min = 1;
	private int _max = 1;

	/**
	 * 未定義ノードが存在しても良い。_noc, _unq より優先する。
	 */
	private boolean _mad = false;

	/**
	 * 子を持たない未定義ノードなら存在しても良い。
	 */
	private boolean _noc = false;

	/**
	 * 単独の(名前が重複しない)未定義ノードなら存在しても良い。
	 */
	private boolean _unq = false;

	public XFormat(XNode root) throws Exception {
		for(XNode node : root.getChildren()) {
			String name = node.getName();

			if(name.equals("_num")) {
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
			else if(name.equals("_mad")) {
				_mad = true;
			}
			else if(name.equals("_noc")) {
				_noc = true;
			}
			else if(name.equals("_unq")) {
				_unq = true;
			}
			else if(name.equals("_props")) {
				_noc = true;
				_unq = true;
			}
			else {
				_children.put(name, new XFormat(node));
			}
		}
	}

	public void check(XNode root) throws Exception {
		check(root, root.getName());
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
					if(_noc == false && _unq == false) {
						throw new IllegalXFormatException(
								name,
								debugPath,
								"存在してはならないタグです。"
								);
					}
					if(_noc) {
						for(XNode node : m.get(name)) {
							if(1 <= node.getChildren().size()) {
								throw new IllegalXFormatException(
										name,
										debugPath,
										"このタグは子ノードを持てません。 <" + node.getChildren().get(0).getName() + ">..."
										);
							}
						}
					}
					if(_unq && 2 <= m.get(name).size()) {
						throw new IllegalXFormatException(
								name,
								debugPath,
								"複数存在してはらならないタグです。" + m.get(name).size()
								);
					}
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
			int min = format._min;
			int max = format._max;

			if(IntTools.isRange(num, min, max) == false) {
				throw new IllegalXFormatException(
						name,
						debugPath,
						"タグの個数に問題があります。" + num + "(" + min + ":" + max + ")"
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
