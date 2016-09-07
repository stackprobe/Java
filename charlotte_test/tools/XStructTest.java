package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.XNode;
import charlotte.tools.XStruct;

public class XStructTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		XStruct xs = new XStruct();

		xs.add(XNode.load("C:/tmp/hsxs/0001.xml"));
		xs.add(XNode.load("C:/tmp/hsxs/0002.xml"));
		xs.add(XNode.load("C:/tmp/hsxs/0003.xml"));
		xs.add(XNode.load("C:/tmp/hsxs/0004.xml"));
		xs.add(XNode.load("C:/tmp/hsxs/0005.xml"));

		FileTools.writeAllBytes(
				"C:/temp/hsxs.txt",
				StringTools.join("\n", xs.toLines()).getBytes(StringTools.CHARSET_UTF8)
				);

		FileTools.writeAllBytes(
				"C:/temp/hsxs.xml",
				StringTools.join("\n", xs.toXmlLines()).getBytes(StringTools.CHARSET_UTF8)
				);
	}

	private static void test02() throws Exception {
		XStruct xs = new XStruct();

		for(String xmlFile : FileTools.ls("C:/tmp/ksz_kkg")) {
			xs.add(XNode.load(xmlFile));
		}
		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg.txt",
				StringTools.join("\n", xs.toLines()).getBytes(StringTools.CHARSET_UTF8)
				);

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg.xml",
				StringTools.join("\n", xs.toXmlLines()).getBytes(StringTools.CHARSET_UTF8)
				);
	}
}
