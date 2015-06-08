package charlotte_test.test;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;

public class Test02 {
	public static void main(String[] args) {
		try {
			JDialog dlg = new JDialog();
			Container cont = dlg.getContentPane();
			RandomLayout rndLyt = new RandomLayout(cont);

			cont.setLayout(rndLyt);

			rndLyt.addLayoutComponent("", new JTextField());
			rndLyt.addLayoutComponent("", new JTextField());
			rndLyt.addLayoutComponent("", new JTextField());

			dlg.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					System.out.println("winClosed");
				}
			});

			dlg.setModal(true);
			dlg.setVisible(true);
			dlg.dispose();

			System.out.println("end");

			System.exit(0);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
