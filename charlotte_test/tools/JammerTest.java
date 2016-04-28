package charlotte_test.tools;

import charlotte.tools.ArrayTools;
import charlotte.tools.DebugTools;
import charlotte.tools.Jammer;
import charlotte.tools.MathTools;

public class JammerTest {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		test01_b(0);

		for(int c = 0; c < 100; c++) {
			test01_b(MathTools.random(10));
		}
		for(int c = 0; c < 100; c++) {
			test01_b(MathTools.random(3000));
		}
		for(int c = 0; c < 100; c++) {
			test01_b(MathTools.random(1000000));
		}
	}

	private static void test01_b(int size) throws Exception {
		System.out.println("size: " + size);

		byte[] src = DebugTools.makeRandBlock(size);
		byte[] enc = Jammer.encode(src);
		byte[] dec = Jammer.decode(enc);

		if(ArrayTools.isSame(src, enc)) {
			throw new Exception("ng");
		}
		if(ArrayTools.isSame(src, dec) == false) {
			throw new Exception("ng");
		}
	}
}
