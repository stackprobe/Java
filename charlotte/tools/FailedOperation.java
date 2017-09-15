package charlotte.tools;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class FailedOperation extends RuntimeException {
	public static final FailedOperation i = new FailedOperation();

	public FailedOperation() {
		super("失敗しました。");
	}

	public FailedOperation(String message) {
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
		else if(e instanceof FailedOperation) {
			JOptionPane.showMessageDialog(
					parent,
					getMessage(e),
					title + " - 失敗",
					JOptionPane.WARNING_MESSAGE
					);
		}
		else {
			e.printStackTrace();

			JOptionPane.showMessageDialog(
					parent,
					getMessage(e),
					title + " - エラー",
					JOptionPane.ERROR_MESSAGE
					);
		}
	}

	public static String getMessage(Throwable e) {
		List<String> lines = new ArrayList<String>();

		while(e != null) {
			String line = e.getMessage();

			if(StringTools.isEmpty(line)) {
				line = e.getClass().getName();
			}
			lines.add(line);

			e = e.getCause();
		}
		return StringTools.join("\n", lines);
	}
}
