package charlotte.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DebugTools {
	public static <T> void printList(String title, Iterable<T> list) {
		System.out.println(title + " {");

		for(T obj : list) {
			System.out.println("\t" + obj);
		}
		System.out.println("}");
	}

	public static ComponentData getHierarchy(Component comp) {
		ComponentData ret = new ComponentData();
		ret.comp = comp;
		ret.parent = null;
		ret.children = new ArrayList<ComponentData>();
		ret.location = comp.getLocation();
		ret.size = comp.getSize();
		ret.name = comp.getName();

		if(comp instanceof Container) {
			Container cont = (Container)comp;
			Component[] children = cont.getComponents();

			for(Component child : children) {
				ComponentData childData = getHierarchy(child);
				childData.parent = ret;
				ret.children.add(childData);
			}
		}
		return ret;
	}

	public static Bmp getVisual(List<ComponentData> compl) {
		Bmp bmp = new Bmp(3500, 1200, Color.WHITE);

		for(ComponentData comp : compl) {
			int l = comp.getScreenLocation().x;
			int t = comp.getScreenLocation().y;
			int w = comp.size.width;
			int h = comp.size.height;
			int r = l + w;
			int b = t + h;

			for(int x = l; x <= r; x++) {
				bmp.setDot(x, t, new Bmp.Dot(Color.RED));
				bmp.setDot(x, b, new Bmp.Dot(Color.BLUE));
			}
			for(int y = t; y <= b; y++) {
				bmp.setDot(l, y, new Bmp.Dot(Color.RED));
				bmp.setDot(r, y, new Bmp.Dot(Color.BLUE));
			}
		}
		return bmp;
	}

	public static String getString(ComponentData comp) {
		return StringTools.join("\n", getStrings(comp, "", null));
	}

	private static List<String> getStrings(ComponentData comp, String indent, List<String> dest) {
		if(dest == null) {
			dest = new ArrayList<String>();
		}
		dest.add(indent + comp);

		if(1 <= comp.children.size()) {
			dest.add(indent + "{");

			for(ComponentData child : comp.children) {
				getStrings(child, indent + "\t", dest);
			}
			dest.add(indent + "}");
		}
		return dest;
	}

	public static class ComponentData {
		public Component comp;
		public ComponentData parent;
		public List<ComponentData> children;
		public Point location;
		public Dimension size;
		public String name;

		public Point getScreenLocation() {
			int l = location.x;
			int t = location.y;

			if(parent != null) {
				Point sl = parent.getScreenLocation();

				l += sl.x;
				t += sl.y;
			}
			return new Point(l, t);
		}

		public List<ComponentData> getUnder() {
			return getUnder(null);
		}

		public List<ComponentData> getUnder(List<ComponentData> dest) {
			dest = getAll(dest);
			dest.remove(0);
			return dest;
		}

		public List<ComponentData> getAll() {
			return getAll(null);
		}

		public List<ComponentData> getAll(List<ComponentData> dest) {
			if(dest == null) {
				dest = new ArrayList<ComponentData>();
			}
			dest.add(this);

			for(ComponentData child : children) {
				child.getAll(dest);
			}
			return dest;
		}

		public boolean isSkewered(Point p) {
			Point sl = getScreenLocation();

			return sl.x <= p.x &&
					sl.y <= p.y &&
					p.x < sl.x + size.width &&
					p.y < sl.y + size.height;
		}

		public List<ComponentData> getSkewered(Point p) {
			List<ComponentData> dest = new ArrayList<ComponentData>();

			for(ComponentData comp : getAll()) {
				if(comp.isSkewered(p)) {
					dest.add(comp);
				}
			}
			return dest;
		}

		@Override
		public String toString() {
			return comp.toString() + "[" + name + "]" + getScreenLocation().x + "," + getScreenLocation().y + "," + size.width + "," + size.height;
		}
	}

	public static void writeHierarchyVisual(Component root, String wPngFile) throws Exception {
		getVisual(getHierarchy(root).getAll()).writeToFile(wPngFile);
	}

	public static void writeHierarchyString(Component root, String wTextFile) throws Exception {
		FileTools.writeAllBytes(wTextFile, getString(getHierarchy(root)).getBytes(StringTools.CHARSET_SJIS));
	}
}
