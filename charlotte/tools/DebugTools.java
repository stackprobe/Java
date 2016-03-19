package charlotte.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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

	public static void snapshot(Component root, String wDir, int wIndex) throws Exception {
		String file = FileTools.combine(wDir, TimeData.now().getString() + StringTools.zPad(wIndex, 3));

		writeHierarchyVisual(root, file + ".png");
		writeHierarchyString(root, file + ".txt");
	}

	private static final String DEF_OUT_DIR = "C:/temp";

	public static void snapshot(Component root) throws Exception {
		snapshot(root, DEF_OUT_DIR, 0);
	}

	public static void snapshot() throws Exception {
		int wIndex = 1;

		for(Window win : getAllWindow()) {
			snapshot(win, DEF_OUT_DIR, wIndex);
			wIndex++;
		}
	}

	public static List<Window> getAllWindow() throws Exception {
		/*
		List<Window> dest = new ArrayList<Window>();

		for(AppContext ac : AppContext.getAppContexts()) {
			Vector<WeakReference<Window>> windowList = (Vector<WeakReference<Window>>)ac.get(Window.class);

			if(windowList != null) {
				for(WeakReference<Window> wr : windowList) {
					Window win = wr.get();

					dest.add(win);
				}
			}
		}
		return dest;
		/*/
		List<Window> dest = new ArrayList<Window>();

		Set<?> appContextSet = (Set<?>)ReflecTools.invokeDeclaredMethod(
				"sun.awt.AppContext",
				"getAppContexts",
				null,
				new Object[0]
				);

		for(Object acObj : appContextSet) {
			Object windowListObj = ReflecTools.invokeDeclaredMethod(
					acObj.getClass(),
					"get",
					acObj,
					new Object[] { Window.class },
					new Class<?>[] { Object.class }
					);

			Vector<WeakReference<Window>> windowList = (Vector<WeakReference<Window>>)windowListObj;

			if(windowList != null) {
				for(WeakReference<Window> wr : windowList) {
					Window win = wr.get();

					dest.add(win);
				}
			}
		}
		return dest;
		//*/
	}

	public static void makeRandTextFile(String file, String charset, String chrs, String newLine, int linecnt, int chrcntMin, int chrcntMax) throws Exception {
		byte[] bNewLine = newLine.getBytes(charset);
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
		try {
			for(int index = 0; index < linecnt; index++) {
				fos.write(makeRandString(chrs, MathTools.random(chrcntMin, chrcntMax)).getBytes(charset));
				fos.write(bNewLine);
			}
		}
		finally {
			FileTools.close(fos);
		}
	}

	public static String makeRandString(String chrs, int chrcnt) {
		StringBuffer buff = new StringBuffer();

		for(int index = 0; index < chrcnt; index++) {
			buff.append(chrs.charAt(MathTools.random(chrs.length())));
		}
		return buff.toString();
	}

	private static final int FILE_COPY_BUFF_SIZE = 10000000; // 10 MB !!!

	public static void copyFile(String rFile, String wFile) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(rFile);
			fos = new FileOutputStream(wFile);

			byte[] buff = new byte[FILE_COPY_BUFF_SIZE];

			for(; ; ) {
				int readSize = fis.read(buff);

				if(readSize < 0) {
					break;
				}
				fos.write(buff, 0, readSize);
			}
		}
		finally {
			FileTools.close(fis);
			FileTools.close(fos);
		}
	}

	public static boolean isSameFile(String file1, String file2) throws Exception {
		BufferedInputStream fis1 = null;
		BufferedInputStream fis2 = null;
		try {
			fis1 = new BufferedInputStream(new FileInputStream(file1));
			fis2 = new BufferedInputStream(new FileInputStream(file2));

			for(; ; ) {
				int chr1 = fis1.read();
				int chr2 = fis2.read();

				if(chr1 != chr2) {
					return false;
				}
				if(chr1 == -1) {
					break;
				}
			}
		}
		finally {
			FileTools.close(fis1);
			FileTools.close(fis2);
		}
		return true;
	}
}
