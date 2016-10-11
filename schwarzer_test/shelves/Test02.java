package schwarzer_test.shelves;

import charlotte.tools.XNode;
import charlotte.tools.XResource;
import schwarzer.shelves.ShelvesDesign;
import schwarzer.shelves.ShelvesDialog;

public class Test02 {
	public static void main(String[] args) {
		try {
			test01("2-Button");
			test01("3-Button");

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01(String footerPrm) throws Exception {
		XNode xDesign = new XResource.Resource() {
			@Override
			public boolean adopt(XNode node, String prm) {
				return footerPrm.equals(prm);
			}

			@Override
			public Class<?> getBaseClassObj() {
				return Test02.class;
			}

			@Override
			public String getBaseRelPath() {
				return "test02/format";
			}

			@Override
			public String getRootRelPath() {
				return "Design.xml";
			}
		}
		.load();

		ShelvesDesign design = new ShelvesDesign(xDesign) {
			@Override
			public void load() {
				// noop
			}
		};

		ShelvesDialog dlg = new ShelvesDialog(null, design);

		dlg.perform();
	}
}
