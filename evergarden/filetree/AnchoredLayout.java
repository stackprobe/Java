package evergarden.filetree;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

public class AnchoredLayout implements LayoutManager {
	private Container _parent;
	private int _w;
	private int _h;

	public AnchoredLayout(Container parent, int w, int h) {
		_parent = parent;
		_w = w;
		_h = h;
	}

	private class ComponentInfo {
		public Component comp;
		public int l;
		public int t;
		public int w;
		public int h;
		public boolean anchorL;
		public boolean anchorT;
		public boolean anchorR;
		public boolean anchorB;
	}

	private List<ComponentInfo> _children = new ArrayList<ComponentInfo>();

	public void add(
			Component comp,
			int l, int t, int w, int h,
			boolean anchorL, boolean anchorT, boolean anchorR, boolean anchorB
			) {
		ComponentInfo ci = new ComponentInfo();

		ci.comp = comp;
		ci.l = l;
		ci.t = t;
		ci.w = w;
		ci.h = h;
		ci.anchorL = anchorL;
		ci.anchorT = anchorT;
		ci.anchorR = anchorR;
		ci.anchorB = anchorB;

		_parent.add(comp);
		_children.add(ci);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// ignore
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// ignore
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(_w, _h);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(0, 0);
		//return new Dimension(_w, _h);
	}

	@Override
	public void layoutContainer(Container parent) {
		int currW = parent.getWidth();
		int currH = parent.getHeight();

		int diffX = currW - _w;
		int diffY = currH - _h;

		for(ComponentInfo child : _children) {
			double addL = 0.0;
			double addT = 0.0;
			double addW = 0.0;
			double addH = 0.0;

			switch((child.anchorL ? 1 : 0) | (child.anchorR ? 2 : 0)) {
			case 0: addL = 0.5; addW = 0.0; break;
			case 1: addL = 0.0; addW = 0.0; break;
			case 2: addL = 1.0; addW = 0.0; break;
			case 3: addL = 0.0; addW = 1.0; break;
			}

			switch((child.anchorT ? 1 : 0) | (child.anchorB ? 2 : 0)) {
			case 0: addT = 0.5; addH = 0.0; break;
			case 1: addT = 0.0; addH = 0.0; break;
			case 2: addT = 1.0; addH = 0.0; break;
			case 3: addT = 0.0; addH = 1.0; break;
			}

			child.comp.setLocation(
					child.l + (int)(addL * diffX),
					child.t + (int)(addT * diffY)
					);
			child.comp.setSize(new Dimension(
					child.w + (int)(addW * diffX),
					child.h + (int)(addH * diffY)
					));
		}
	}
}
