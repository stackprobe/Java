package schwarzer.shelves.shelf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import schwarzer.shelves.Shelf;

public class DummyShelf extends Shelf {
	private JLabel label;
	private JPanel panel;

	{
		label = new JLabel("Dummy");
		label.setHorizontalAlignment(JLabel.CENTER);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		panel.setBackground(Color.CYAN);
	}

	@Override
	public Component getComponent() {
		return panel;
	}

	@Override
	public void setValue(Object value) {
		label.setText((String)value);
	}

	@Override
	public Object getValue() {
		return label.getText();
	}
}
