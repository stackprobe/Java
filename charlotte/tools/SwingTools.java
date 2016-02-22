package charlotte.tools;

import javax.swing.SwingUtilities;

public class SwingTools {
	public static void invokeLaterDeep(Runnable runner) {
		invokeLaterDeep(runner, 100, 1L);
	}

	public static void invokeLaterDeep(final Runnable runner, final int deep, final long millis) {
		try {
			if(1 <= deep) {
				ThreadTools.sleep(millis);

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
