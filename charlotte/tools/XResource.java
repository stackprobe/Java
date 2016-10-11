package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public abstract class XResource {
	public static final String LINK_NODE_NAME = "_xResLink";
	public static final String LINKED_PATH_NODE_NAME = "SubPath";
	public static final String OPTION_NODE_NAME = "_xResOption";
	public static final String OPTION_PRM_NODE_NAME = "_option";

	public abstract XNode getRoot();
	public abstract boolean isLink(XNode node);
	public abstract XNode getLinkedRoot(XNode node);
	public abstract boolean isOption(XNode node);
	public abstract List<XNode> adoptOption(XNode node);

	public XNode load() {
		XNode root = getRoot();
		QueueData<XNode> q = new QueueData<XNode>();

		q.add(root);

		for(; ; ) {
			XNode parent = q.poll();

			if(parent == null) {
				break;
			}
			for(int index = 0; index < parent.getChildren().size(); index++) {
				XNode child = parent.getChildren().get(index);

				if(isLink(child)) {
					solveLink(parent, index, child);
					index--;
				}
				else if(isOption(child)) {
					solveOption(parent, index, child);
					index--;
				}
				else {
					q.add(child);
				}
			}
		}
		return root;
	}

	private void solveLink(XNode parent, int index, XNode child) {
		XNode root = getLinkedRoot(child);

		parent.getChildren().remove(index);
		parent.getChildren().addAll(index, root.getChildren());
	}

	private void solveOption(XNode parent, int index, XNode child) {
		List<XNode> adopted = adoptOption(child);
		if(adopted == null) {
			adopted = new ArrayList<XNode>();
		}

		parent.getChildren().remove(index);
		parent.getChildren().addAll(index, adopted);
	}

	public static abstract class XResource2 extends XResource {
		public abstract boolean adopt(XNode node, String prm);

		@Override
		public boolean isLink(XNode node) {
			return LINK_NODE_NAME.equalsIgnoreCase(node.getName());
		}

		@Override
		public boolean isOption(XNode node) {
			return OPTION_NODE_NAME.equalsIgnoreCase(node.getName());
		}

		@Override
		public List<XNode> adoptOption(XNode node) {
			return adopt(node, node.getNodeValue(OPTION_PRM_NODE_NAME)) ?
					XNode.except(node.getChildren(), OPTION_PRM_NODE_NAME) :
					null;
		}
	}

	public static abstract class File extends XResource2 {
		public abstract String getFile();
		public abstract String getSubRootDir();

		@Override
		public XNode getRoot() {
			try {
				return XNode.load(getFile());
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}

		@Override
		public XNode getLinkedRoot(XNode node) {
			try {
				String relPath = node.getNodeValue(LINKED_PATH_NODE_NAME);
				String file = FileTools.combine(getSubRootDir(), relPath);

				return XNode.load(file);
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}
	}

	public static abstract class Resource extends XResource2 {
		public abstract Class<?> getBaseClassObj();
		public abstract String getBaseRelPath();
		public abstract String getRootRelPath();

		@Override
		public XNode getRoot() {
			try {
				Class<?> rootClassObj = getBaseClassObj();
				String relPath = FileTools.combine(
						getBaseRelPath(),
						getRootRelPath()
						);

				return XNode.load(FileTools.readToEnd(rootClassObj.getResource(relPath)));
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}

		@Override
		public XNode getLinkedRoot(XNode node) {
			try {
				Class<?> rootClassObj = getBaseClassObj();
				String relPath = FileTools.combine(
						getBaseRelPath(),
						node.getNodeValue(LINKED_PATH_NODE_NAME)
						);

				return XNode.load(FileTools.readToEnd(rootClassObj.getResource(relPath)));
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}
	}
}
