package evergarden.filetree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class FileTreeModel implements TreeModel {
	public static class Node {
		public String path;

		public Node(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			String ret = FileTools.getLocal(path);

			if(ret.length() == 0) {
				ret = path;
			}
			return ret;
		}
	}

	public static Comparator<Node> compNode = new Comparator<Node>() {
		@Override
		public int compare(Node a, Node b) {
			return StringTools.compIgnoreCase.compare(a.path, b.path);
		}
	};

	private Node _root;

	public FileTreeModel(String rootDir) {
		_root = new Node(rootDir);
	}

	@Override
	public Object getRoot() {
		return _root;
	}

	private List<Node> getChildren(Object parent) {
		String dir = ((Node)parent).path;
		List<String> paths;
		try {
			paths = FileTools.ls(dir);
		}
		catch(Throwable e) {
			//e.printStackTrace(System.out);
			paths = new ArrayList<String>();
		}
		ArrayTools.sort(paths, StringTools.compIgnoreCase);
		List<Node> nodes = new ArrayList<Node>();

		for(String path : paths) {
			nodes.add(new Node(path));
		}
		return nodes;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return getChildren(parent).get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return getChildren(parent).size();
	}

	@Override
	public boolean isLeaf(Object node) {
		return FileTools.isDirectory(((Node)node).path) == false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// noop
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		List<Node> nodes = getChildren(parent);
		return ArrayTools.indexOf(nodes, (Node)child, compNode);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// ignore
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// ignore
	}
}
