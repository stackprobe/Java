package evergarden.schwarzer.shelves.shelf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import evergarden.schwarzer.shelves.Shelf;
import evergarden.schwarzer.shelves.ShelvesDialog;

public class Heading extends Shelf {
	public String labelText = "未定義";

	@Override
	public Component getComponent() {
		if(panel == null) {
			createComponent();
		}
		return panel;
	}

	private JPanel panel;
	private JLabel label;
	private int lineL;

	private void createComponent() {
		panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(Color.GRAY);
				g.drawLine(
						lineL,
						panel.getHeight() / 2,
						panel.getWidth(),
						panel.getHeight() / 2
						);
			}
		};

		ShelvesDialog.AncLayoutMgr layout = new ShelvesDialog.AncLayoutMgr(panel);
		panel.setLayout(layout);

		label = new JLabel();
		label.setText(labelText);

		int w = label.getPreferredSize().width;

		layout.add(
				label,
				0,
				0,
				w,
				0,
				true,
				true,
				false,
				true
				);

		lineL = w + 5;
	}

	@Override
	public void setValue(Object value) {
		// noop
	}

	@Override
	public Object getValue() {
		return null;
	}
}
