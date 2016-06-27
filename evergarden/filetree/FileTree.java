package evergarden.filetree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class FileTree extends JTree {
	private JPopupMenu _pm;

	public FileTree() {
		_pm = new JPopupMenu();
		_pm.add(createMenuItem("選択されているアイテムを開く", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("*"); // TODO
			}
		}));
		_pm.add(createMenuItem("ダミーアイテム1", null));
		_pm.add(createMenuItem("ダミーアイテム2", null));
		_pm.add(createMenuItem("ダミーアイテム3", null));

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
					if(e.getClickCount() == 2) {
						leafAction();
					}
					break;
				case MouseEvent.BUTTON3:
					rClicked(e);
					break;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// noop
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// noop
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// noop
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// noop
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// noop
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					leafAction();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// noop
			}
		});
	}

	private static JMenuItem createMenuItem(String title, ActionListener al) {
		JMenuItem ret = new JMenuItem(title);
		ret.addActionListener(al);
		return ret;
	}

	private void rClicked(MouseEvent e) {
		// TODO 未選択なら選択
		_pm.show(e.getComponent(), e.getX(), e.getY());
	}

	private void leafAction() {
		for(TreePath tp : getSelectionPaths()) {
			if(getModel().isLeaf(tp.getLastPathComponent())) {
				leafAction(tp.getLastPathComponent());
			}
		}
	}

	private void leafAction(Object node) {
		System.out.println("leafAction: " + ((FileTreeModel.Node)node).path);
	}
}
