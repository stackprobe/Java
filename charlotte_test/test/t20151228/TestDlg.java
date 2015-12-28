package charlotte_test.test.t20151228;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
			FixPosLayout layout = new FixPosLayout(panel);
			panel.setLayout(layout);

			final JTextField tf = new JTextField();

			layout.add(tf, 10, 10, 200, 30);

			final JComboBox cb = new JComboBox();

			cb.removeAllItems();

			for(char chr : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
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
					if(e.getKeyCode() != KEY_ENTER) {
						pulsar.set();
					}
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

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					_flag = false;
				}
			});
		}

		public boolean check() {
			return _flag;
		}
	}

	private class FixPosLayout implements LayoutManager {
		private Container _parent;
		private int _w;
		private int _h;

		public FixPosLayout(Container parent) {
			_parent = parent;
		}

		public FixPosLayout(Container parent, int w, int h) {
			_parent = parent;
			_w = w;
			_h = h;
		}

		public void setSize(int w, int h) {
			_w = w;
			_h = h;
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// unimpl
		}

		@Override
		public Dimension minimumLayoutSize(Container cont) {
			return new Dimension(0, 0);
			//return new Dimension(_w, _h);
		}

		@Override
		public Dimension preferredLayoutSize(Container cont) {
			return new Dimension(_w, _h);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			// unimpl
		}

		private class ComponentInfo {
			public Component comp;
			public int l;
			public int t;
			public int w;
			public int h;
		}

		private List<ComponentInfo> _children = new ArrayList<ComponentInfo>();

		public void add(Component comp, int l, int t, int w, int h) {
			ComponentInfo child = new ComponentInfo();

			child.comp = comp;
			child.l = l;
			child.t = t;
			child.w = w;
			child.h = h;

			_children.add(child);
			_parent.add(comp);
		}

		@Override
		public void layoutContainer(Container cont) {
			for(ComponentInfo child : _children) {
				child.comp.setLocation(child.l, child.t);
				child.comp.setSize(child.w, child.h);
			}
		}
	}
}
