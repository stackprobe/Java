package charlotte.tools;

import java.awt.Component;

import javax.swing.JOptionPane;

public class FaultOperation extends Exception {
	public static final FaultOperation i = new FaultOperation();

	public FaultOperation() {
		super();
	}

	public FaultOperation(String message) {
		super(message);
	}

	public static void caught(Component parent, Throwable e, String title) {
		if(e instanceof Completed) {
			JOptionPane.showMessageDialog(
					parent,
					"完了しました。",
					title + " - 完了",
					JOptionPane.INFORMATION_MESSAGE
					);
		}
		else if(e instanceof Ended) {
			// noop
		}
		else if(e instanceof Cancelled) {
			JOptionPane.showMessageDialog(
					parent,
					"中止しました。",
					title + " - 中止",
					JOptionPane.WARNING_MESSAGE
					);
		}
		else if(e instanceof FaultOperation) {
			String message = e.getMessage();

			if(StringTools.isEmpty(message)) {
				message = "失敗しました。";
			}
			JOptionPane.showMessageDialog(
					parent,
					message,
					title + " - 失敗",
					JOptionPane.WARNING_MESSAGE
					);
		}
		else {
			e.printStackTrace();

			String message = e.getMessage();

			if(StringTools.isEmpty(message)) {
				message = e.getClass().getName();
			}
			JOptionPane.showMessageDialog(
					parent,
					message,
					title + " - エラー",
					JOptionPane.ERROR_MESSAGE
					);
		}
	}
}
