package schwarzer.shelves.shelf;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import charlotte.tools.XNode;
import schwarzer.shelves.Shelf;
import schwarzer.shelves.ShelvesDialog;

public class LabelledComboBox extends Shelf {
	public String labelText = "未定義";
	public int labelWidth = 100;
	public XNode extra = new XNode();

	private List<Item> items;

	private static class Item {
		public String text;
		public String value;
		public boolean selected;
	}

	private void init() {
		items = new ArrayList<Item>();

		for(XNode node : extra.getNodes("items/item")) {
			Item item = new Item();

			item.text = node.getNodeValue("text").toString();
			item.value = node.getNodeValue("value").toString();
			item.selected = node.hasNode("selected");

			items.add(item);
		}
	}

	@Override
	public Component getComponent() {
		if(panel == null) {
			init();
			createComponent();
		}
		return panel;
	}

	private JPanel panel;
	private JLabel label;
	private JComboBox<String> field;

	private void createComponent() {
		panel = new JPanel();
		ShelvesDialog.AncLayoutMgr layout = new ShelvesDialog.AncLayoutMgr(panel);
		panel.setLayout(layout);

		label = new JLabel();
		label.setText(labelText + "：");

		layout.add(
				label,
				0,
				0,
				labelWidth,
				0,
				true,
				true,
				false,
				true
				);

		field = new JComboBox<String>();
		field.setEditable(false);

		int selectedIndex = -1;

		for(int index = 0; index < items.size(); index++) {
			Item item = items.get(index);

			field.addItem(item.text);

			if(item.selected) {
				selectedIndex = index;
			}
		}
		field.setSelectedIndex(selectedIndex);

		layout.add(
				field,
				labelWidth,
				0,
				0,
				0,
				true,
				true,
				true,
				true
				);
	}

	@Override
	public void setValue(Object value) {
		for(int index = 0; index < items.size(); index++) {
			Item item = items.get(index);

			if(item.value.equals(value)) {
				field.setSelectedIndex(index);
				break;
			}
		}
	}

	@Override
	public Object getValue() {
		int index = field.getSelectedIndex();

		if(index == -1) {
			return null;
		}
		return items.get(index).value;
	}
}
