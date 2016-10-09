package schwarzer.shelves.shelf;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import charlotte.tools.StringTools;
import charlotte.tools.XNode;
import schwarzer.shelves.Shelf;
import schwarzer.shelves.ShelvesDialog;

public class LabelledComboBox extends Shelf {
	public String labelText = "未定義：";
	public int labelWidth = 100;
	public XNode items = new XNode();

	private static final String ITEM_TEXT = "text";
	private static final String ITEM_VALUE = "value";
	private static final String ITEM_SELECTED = "selected";

	@Override
	public Component getComponent() {
		if(panel == null) {
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
		label.setText(labelText);

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

		int selectedIndex = -1;

		for(int index = 0; index < items.getChildren().size(); index++) {
			XNode item = items.getChildren().get(index);
			String text = item.getNodeValue(ITEM_TEXT);
			String value = item.getNodeValue(ITEM_VALUE);
			boolean selected = StringTools.toFlag(item.getNodeValue(ITEM_SELECTED));

			field.addItem(text);

			if(selected) {
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
		for(int index = 0; index < items.getChildren().size(); index++) {
			XNode item = items.getChildren().get(index);

			if(item.getNodeValue(ITEM_VALUE).equals(value)) {
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
		return items.getChildren().get(index).getNodeValue(ITEM_VALUE);
	}
}
