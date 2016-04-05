package charlotteLabo.mp4;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.StringTools;

public class Box {
	private static final String ROOT_NAME = "root";

	public String name;
	public byte[] image;
	public List<Box> boxes;

	public static Box create(String file) throws Exception {
		return create(FileTools.readAllBytes(file));
	}

	public static Box create(byte[] image) {
		Box root = new Box();

		root.name = ROOT_NAME;
		root.image = image;
		root.boxes = parseBoxes(image);

		return root;
	}

	public static List<Box> parseBoxes(byte[] image) {
		List<Box> boxes = new ArrayList<Box>();

		try {
			int rPos = 0;

			while(rPos < image.length) {
				if(image.length - rPos < 8) {
					throw null;
				}
				int size = IntTools.toInt(image, rPos);

				size = IntTools.revEndian(size);

				if(size < 8) {
					throw null;
				}
				if(image.length - rPos < size) {
					throw null;
				}
				Box box = new Box();

				box.name = new String(ArrayTools.getBytes(image, rPos + 4, 4), StringTools.CHARSET_ASCII);
				box.image = ArrayTools.getBytes(image, rPos + 8, size - 8);
				box.boxes = parseBoxes(box.image);

				boxes.add(box);

				rPos += size;
			}
		}
		catch(Throwable e) {
			//e.printStackTrace(System.out);

			boxes.clear();
		}
		return boxes;
	}

	// ---- debug print ----

	public void debugPrint() {
		debugPrint(0);
	}

	public void debugPrint(int indent) {
		debugPrint(indent, "[" + name + "] " + image.length);

		for(Box box : boxes) {
			box.debugPrint(indent + 1);
		}
	}

	private void debugPrint(int indent, String line) {
		for(int c = 0; c < indent; c++) {
			System.out.print("\t");
		}
		System.out.println(line);
	}

	// ----

	public List<Box> find(String name) {
		List<Box> dest = new ArrayList<Box>();

		for(Box box : boxes) {
			if(box.name.equals(name)) {
				dest.add(box);
			}
		}
		System.out.println("find [" + name + "] -> " + dest.size()); // test
		return dest;
	}

	public Box get(String name) {
		return find(name).get(0);
	}
}
