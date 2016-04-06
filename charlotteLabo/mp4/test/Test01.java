package charlotteLabo.mp4.test;

import charlotteLabo.mp4.Box;
import charlotteLabo.mp4.detail.mvhd;
import charlotteLabo.mp4.detail.stts;

public class Test01 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		test01_b("C:/var/mp4/ddd.mp4");

		/*
		for(String file : FileTools.ls("C:/var/mp4")) {
			test01_b(file);
		}
		*/
	}

	private static void test01_b(String file) throws Exception {
		System.out.println("< " + file);

		Box root = Box.create(file);
		root.debugPrint();

		{
			mvhd i = new mvhd();
			i.load(root.get("moov/mvhd"));

			System.out.println("[mvhd]");
			System.out.println("" + i.version);
			System.out.println("" + i.timeScale);
			System.out.println("" + i.duration);
			System.out.println("" + i.rate);
			System.out.println("" + i.volume);
		}

		for(Box box : root.find("moov/trak/mdia/minf/stbl/stts")) {
			stts i = new stts();
			i.load(box);

			System.out.println("[stts]");
			System.out.println("" + i.version);
			System.out.println("" + i.bytesNumberOfTimes);
			System.out.println("" + i.bytesTimePerFrame_a);
			System.out.println("" + i.bytesTimePerFrame_b);
		}
	}
}
