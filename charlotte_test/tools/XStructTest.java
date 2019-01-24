package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.XNode;
import charlotte.tools.XStruct;

public class XStructTest {
	public static void main(String[] args) {
		try {
			//test01();
			//test02();
			//test02("C:/tmp/KKC401_res_edit");
			test02("C:/tmp/KKC401_res_edit_ref");

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
		test02("C:/tmp/ksz_kkg");
	}

	private static void test02(String dir) throws Exception {
		XStruct xs = new XStruct();

		for(String xmlFile : FileTools.ls(dir)) {
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

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_path.txt",
				StringTools.join("\n", xs.toPathLines()).getBytes(StringTools.CHARSET_SJIS)
				);

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_path_value.txt",
				StringTools.join("\n", xs.toPathValueLines()).getBytes(StringTools.CHARSET_SJIS)
				);

		XStruct.valueListMax = Integer.MAX_VALUE;

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_full.txt",
				StringTools.join("\n", xs.toLines()).getBytes(StringTools.CHARSET_UTF8)
				);

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_full.xml",
				StringTools.join("\n", xs.toXmlLines()).getBytes(StringTools.CHARSET_UTF8)
				);

		/*
		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_path_full.txt",
				StringTools.join("\n", xs.toPathLines()).getBytes(StringTools.CHARSET_SJIS)
				);
				*/

		FileTools.writeAllBytes(
				"C:/temp/ksz_kkg_path_value_full.txt",
				StringTools.join("\n", xs.toPathValueLines()).getBytes(StringTools.CHARSET_SJIS)
				);
	}
}
