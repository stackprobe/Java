package charlotte.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ActionListenerEx {
	public abstract void actionPerformed() throws Exception;

	public ActionListener getActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ActionListenerEx.this.actionPerformed();
				}
				catch(Throwable ex) {
					ex.printStackTrace();
				}
			}
		};
	}
}
