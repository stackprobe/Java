package schwarzer.shelves.shelf;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import schwarzer.shelves.Shelf;
import schwarzer.shelves.ShelvesDialog;

public class LabelledTextField extends Shelf {
	public String title = "未定義：";
	public int titleWidth = 100;

	@Override
	public Component getComponent() {
		if(panel == null) {
			createComponent();
		}
		return panel;
	}

	private JPanel panel;
	private JLabel label;
	private JTextField field;

	private void createComponent() {
		panel = new JPanel();
		ShelvesDialog.AncLayoutMgr layout = new ShelvesDialog.AncLayoutMgr(panel);
		panel.setLayout(layout);

		label = new JLabel();
		label.setText(title);

		layout.add(
				label,
				0,
				0,
				titleWidth,
				0,
				true,
				true,
				false,
				true
				);

		field = new JTextField();

		layout.add(
				field,
				titleWidth,
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
	public void setValue(String value) {
		field.setText(value);
	}

	@Override
	public String getValue() {
		return field.getText();
	}
}
