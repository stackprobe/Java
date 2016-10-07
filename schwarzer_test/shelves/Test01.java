package schwarzer_test.shelves;

import schwarzer.shelves.Button;
import schwarzer.shelves.Column;
import schwarzer.shelves.Form;
import schwarzer.shelves.Header;
import schwarzer.shelves.ShelvesDialog;
import schwarzer.shelves.ShelvesManager;
import schwarzer.shelves.Tab;
import schwarzer.shelves.shelf.DummyShelf;

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

	private static void test01() {
		ShelvesManager mgr = new ShelvesManager() {
			@Override
			public Form getForm() {
				Form form = new Form();

				form.header = new Header();
				form.header.buttons.add(new Button());
				form.header.buttons.add(new Button());
				form.header.buttons.add(new Button());
				form.header.align = "left";

				form.footer = new Header();
				form.footer.buttons.add(new Button());
				form.footer.buttons.add(new Button());
				form.footer.buttons.add(new Button());
				form.footer.buttons.add(new Button());
				form.footer.buttons.add(new Button());
				form.footer.align = "right";

				{
					Tab tab = new Tab();

					{
						Column column = new Column();

						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());

						tab.columns.add(column);
					}

					{
						Column column = new Column();

						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());

						tab.columns.add(column);
					}

					form.tabs.add(tab);
				}

				{
					Tab tab = new Tab();

					{
						Column column = new Column();

						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());

						tab.columns.add(column);
					}

					{
						Column column = new Column();

						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());

						tab.columns.add(column);
					}

					{
						Column column = new Column();

						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());
						column.shelves.add(new DummyShelf());

						tab.columns.add(column);
					}

					form.tabs.add(tab);
				}

				return form;
			}

			@Override
			public void load() {
				// noop
			}

			@Override
			public void save() {
				// noop
			}
		};

		ShelvesDialog dlg = new ShelvesDialog(null, mgr);

		dlg.perform();
	}
}
