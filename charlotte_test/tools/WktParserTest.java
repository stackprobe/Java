package charlotte_test.tools;

import charlotte.tools.FileTools;
import charlotte.tools.StringTools;
import charlotte.tools.WktParser;

public class WktParserTest {
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
		WktParser wp = new WktParser();
		wp.add(FileTools.readAllText("C:/var/1-0_template.gcm", StringTools.CHARSET_UTF8));
		WktParser.SubEntities root = wp.getEntities();
		WktParser.SubEntities layers = root.getBlock("LAYERS");

		for(WktParser.SubEntities layer : layers.getBlocks("LAYER")) {
			WktParser.SubEntities dataset = layer.getBlock("DATASET");
			WktParser.SubEntities uriDataset = dataset.getBlock("URI_DATASET");
			WktParser.SubEntities uri = uriDataset.getBlock("URI");
			WktParser.Token uriValue = uri.get(0);

			uriValue.value = uriValue.value.replace("1-0/", "めっさめっさ-0/");
		}
		FileTools.writeAllText("C:/temp/1.wkt", wp.getWkt(), StringTools.CHARSET_UTF8);
	}
}
