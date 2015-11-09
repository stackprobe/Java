package charlotte.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

public class FixPosLayout implements LayoutManager {
	private Container _parent;
	private int _w;
	private int _h;

	public FixPosLayout(Container parent) {
		_parent = parent;
	}

	public FixPosLayout(Container parent, int w, int h) {
		_parent = parent;
		_w = w;
		_h = h;
	}

	public void setSize(int w, int h) {
		_w = w;
		_h = h;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// unimpl
	}

	@Override
	public Dimension minimumLayoutSize(Container cont) {
		return new Dimension(0, 0);
		//return new Dimension(_w, _h);
	}

	@Override
	public Dimension preferredLayoutSize(Container cont) {
		return new Dimension(_w, _h);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// unimpl
	}

	private class ComponentInfo {
		public Component comp;
		public int l;
		public int t;
		public int w;
		public int h;
	}

	private List<ComponentInfo> _children = new ArrayList<ComponentInfo>();

	public void add(Component comp, int l, int t, int w, int h) {
		ComponentInfo child = new ComponentInfo();

		child.comp = comp;
		child.l = l;
		child.t = t;
		child.w = w;
		child.h = h;

		_children.add(child);
		_parent.add(comp);
	}

	@Override
	public void layoutContainer(Container cont) {
		for(ComponentInfo child : _children) {
			child.comp.setLocation(child.l, child.t);
			child.comp.setSize(child.w, child.h);
		}
	}
}
