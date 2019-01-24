package charlotte.tools;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SwingTools {
	public static void invoke(final RunnableEx runner) throws Exception {
		final TabletStore ts = new TabletStore();

		invokeLater(new RunnableEx() {
			@Override
			public void run() throws Exception {
				runner.getRunnable().run();
				ts.add();
			}
		});

		ts.must();
		runner.relay();
	}

	public static void invokeLater(RunnableEx runner) {
		invokeLaterDeep(runner, 0, 0);
	}

	public static void invokeLaterDeep(RunnableEx runner) {
		invokeLaterDeep(runner.getRunnable());
		//runner.relay(); // 拾えません。
	}

	public static void invokeLaterDeep(RunnableEx runner, int deep, long millis) {
		invokeLaterDeep(runner.getRunnable(), deep, millis);
		//runner.relay(); // 拾えません。
	}

	public static void invokeLaterDeep(Runnable runner) {
		invokeLaterDeep(runner, 100, 1L);
	}

	public static void invokeLaterDeep(final Runnable runner, final int deep, final long millis) {
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(millis);

						if(1 <= deep) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									invokeLaterDeep(runner, deep - 1, millis);
								}
							});
						}
						else {
							SwingUtilities.invokeLater(runner);
						}
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			}
			.start();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static int showSaveDialogConfirmOverwrite(JFileChooser fc, Component parent) throws Exception {
		for(; ; ) {
			int ret = fc.showSaveDialog(parent);

			if(ret == JFileChooser.APPROVE_OPTION) {
				String file = fc.getSelectedFile().getCanonicalPath();

				if(FileTools.exists(file)) {
					String[] selectValues = new String[] {
							"上書きする",
							"別の名前を指定する",
							"キャンセル",
							};
					int ret2 = JOptionPane.showOptionDialog(
							parent,
							"入力されたファイル名は既に存在します。",
							"上書き確認",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							selectValues,
							selectValues[0]
							);
					if(ret2 == 1) {
						continue;
					}
					if(ret2 == 2) {
						ret = JFileChooser.CANCEL_OPTION;
					}
				}
			}
			return ret;
		}
	}
}
