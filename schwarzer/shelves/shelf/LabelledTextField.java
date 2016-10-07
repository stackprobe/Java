package schwarzer.shelves.shelf;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextField;

import schwarzer.shelves.Shelf;
import schwarzer.shelves.ShelvesDialog;

public class LabelledTextField extends Shelf {
	private JTextField field;
	private JPanel panel;

	{
		panel = new JPanel();
		ShelvesDialog.AncLayoutMgr layout = new ShelvesDialog.AncLayoutMgr(panel);
		panel.setLayout(layout);

		// TODO
	}

	@Override
	public Component getComponent() {
		return panel;
	}

	@Override
	public void setValue(String value) {
		throw null; // TODO
	}

	@Override
	public String getValue() {
		throw null; // TODO
	}
}
