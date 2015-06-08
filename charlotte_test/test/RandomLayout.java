package charlotte_test.test;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

public class RandomLayout implements LayoutManager {
	private Container _owner;
	private List<Component> _c = new ArrayList<Component>();

	public RandomLayout(Container owner) {
		_owner = owner;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		_c.add(comp);
		_owner.add(comp);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		_c.remove(comp);
		_owner.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(300, 300);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(200, 200);
	}

	@Override
	public void layoutContainer(Container parent) {
		for(Component comp : _c) {
			comp.setLocation(
					(int)(Math.random() * 100),
					(int)(Math.random() * 100)
					);
			comp.setSize(
					(int)(Math.random() * 100),
					(int)(Math.random() * 100)
					);
		}
	}
}
