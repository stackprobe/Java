package charlotte_test.tools;

import java.awt.Window;

import javax.swing.JDialog;

import charlotte.tools.DebugTools;
import charlotte.tools.XText;

public class DebugToolsTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		JDialog dlg1 = new JDialog();
		JDialog dlg2 = new JDialog();
		JDialog dlg3 = new JDialog();

		for(Window win : DebugTools.getAllWindow()) {
			System.out.println("win: " + win);
		}
	}

	private static void test02() {
		test02_b(null);
		test02_b("");
		test02_b("#");
		test02_b("abc");
		test02_b("123456");
		test02_b("C:\\temp");
		test02_b("ゆきだるま=☃");
		test02_b("<h1>It works!</h1>");
		test02_b("\r\n\t");
	}

	private static void test02_b(Object src) {
		String enc = XText.encode(src);

		System.out.println(src + " -> [" + enc + "]");
	}
}
