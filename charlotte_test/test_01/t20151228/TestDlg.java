package charlotte_test.test_01.t20151228;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import charlotte.tools.FixedLayout;
import charlotte.tools.StringTools;

public class TestDlg extends JDialog {
	public static void main(String[] args) {
		try {
			new TestDlg().setVisible(true);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public TestDlg() {
		super();

		setModal(true);

		JPanel panel = new JPanel();

		{
			FixedLayout layout = new FixedLayout(panel);
			panel.setLayout(layout);

			final JTextField tf = new JTextField();

			layout.add(tf, 10, 10, 200, 30);

			final JComboBox cb = new JComboBox();

			cb.removeAllItems();

			for(char chr : StringTools.ALPHA.toCharArray()) {
				cb.addItem("" + chr);
			}
			cb.setSelectedIndex(-1);

			final Pulsar pulsar = new Pulsar();

			cb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					/*
					if(e.getStateChange() == ItemEvent.SELECTED) {
						tf.setText(tf.getText() + e.getItem());
						//cb.setSelectedIndex(-1);
					}
					*/
					System.out.println("" + e);
				}
			});

			cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(cb == e.getSource() &&
							"comboBoxChanged".equals(e.getActionCommand()) &&
							pulsar.check() == false
							) {
						tf.setText(tf.getText() + cb.getSelectedItem());
					}
					System.out.println("" + e);
				}
			});

			cb.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					//System.out.println("" + e);
				}

				private static final int KEY_ENTER = 10;

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KEY_ENTER) {
						tf.setText(tf.getText() + cb.getSelectedItem());
					}
					pulsar.set();
					System.out.println("" + e);
				}

				@Override
				public void keyReleased(KeyEvent e) {
					//System.out.println("" + e);
				}
			});

			layout.add(cb, 10, 50, 200, 30);
		}

		getContentPane().add(panel);

		setSize(300, 200);
	}

	private static class Pulsar {
		private boolean _flag;

		public void set() {
			_flag = true;
			System.out.println("Pulsar set");

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					_flag = false;
					System.out.println("Pulsar un-set");
				}
			});
		}

		public boolean check() {
			return _flag;
		}
	}
}
