package charlotte.tools;

public abstract class XResource {
	public static final String LINK_NODE_NAME = "Link";
	public static final String LINKED_PATH_NODE_NAME = "Path";

	public abstract XNode getRoot();
	public abstract boolean isLink(XNode node);
	public abstract XNode getLinkedRoot(XNode node);

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

	public static abstract class File extends XResource {
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
		public boolean isLink(XNode node) {
			return LINK_NODE_NAME.equalsIgnoreCase(node.getName());
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

	public static abstract class Resource extends XResource {
		public abstract Class<?> getRootClassObj();
		public abstract String getRootRelPath();

		@Override
		public XNode getRoot() {
			try {
				Class<?> rootClassObj = getRootClassObj();
				String relPath = getRootRelPath();

				return XNode.load(FileTools.readToEnd(rootClassObj.getResource(relPath)));
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}

		@Override
		public boolean isLink(XNode node) {
			return LINK_NODE_NAME.equalsIgnoreCase(node.getName());
		}

		@Override
		public XNode getLinkedRoot(XNode node) {
			try {
				Class<?> rootClassObj = getRootClassObj();
				String relPath = node.getNodeValue(LINKED_PATH_NODE_NAME);

				return XNode.load(FileTools.readToEnd(rootClassObj.getResource(relPath)));
			}
			catch(Exception e) {
				throw RunnableEx.re(e);
			}
		}
	}
}
