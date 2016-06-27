package evergarden.filetree;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TestDialog extends JDialog {
	public static void main(String[] args) {
		try {
			new TestDialog().dispose();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public TestDialog() {
		JPanel panel = new JPanel();
		AnchorLayout layout = new AnchorLayout(panel, 1000, 1000);
		panel.setLayout(layout);

		FileTreeModel fileTreeModel = new FileTreeModel("C:/");
		//FileTreeModel fileTreeModel = new FileTreeModel("C:/var");

		FileTree fileTree = new FileTree();
		fileTree.setModel(fileTreeModel);

		JScrollPane fileTreeScrollPane = new JScrollPane();
		fileTreeScrollPane.getViewport().add(fileTree);

		layout.add(fileTreeScrollPane, 10, 10, 980, 930, true, true, true, true);
		layout.add(new JButton("noop"), 300, 950, 400, 40, false, false, false, true);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.setSize(500, 400);
		this.setMinimumSize(this.getSize());
		this.setTitle("ちんこ");
		this.setModal(true);
		this.setVisible(true);
	}
}
