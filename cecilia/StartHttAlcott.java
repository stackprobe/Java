package cecilia;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cecilia.htt.HttAlcott;
import charlotte.htt.HttServer;
import charlotte.satellite.MutexObject;
import charlotte.tools.ActionListenerEx;
import charlotte.tools.FixedLayout;
import charlotte.tools.ThreadTools;

public class StartHttAlcott {
	public static void main(String[] args) {
		try {
			new StartHttAlcott().main2();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void main2() {
		MutexObject mo = new MutexObject(HttAlcott.IDENT + "_dlg");

		if(mo.waitOne(0)) {
			try {
				main3();
			}
			finally {
				mo.release();
			}
		}
	}

	private JLabel _frmStatus;
	private JLabel _dlgStatus;

	private void main3() {
		JFrame frm = new JFrame();

		{
			_frmStatus = new JLabel(STATE_READY, SwingConstants.CENTER);

			JPanel p = new JPanel();

			p.setLayout(new BorderLayout());
			p.add(_frmStatus, BorderLayout.CENTER);

			Container cp = frm.getContentPane();

			cp.setLayout(new BorderLayout());
			cp.add(p, BorderLayout.CENTER);
		}

		frm.setTitle("bluetears");
		frm.setLocation(-200, -200);
		setSize(frm, 100, 100);
		frm.setVisible(true);
		frm.repaint();

		final JDialog dlg = new JDialog(frm);

		{
			JPanel p = new JPanel();

			FixedLayout lo = new FixedLayout(p);
			p.setLayout(lo);

			int l = 10;
			int t = 10;
			int w = 166;
			int h = 40;
			int yg = 10;

			lo.add(createButton(
					"Restart",
					new ActionListenerEx() {
						@Override
						public void actionPerformed() throws Exception {
							start();
							setStatus(STATE_RUNNING);
						}
					}),
					l, t, w, h
					);
			t += h + yg;

			lo.add(createButton(
					"Clear",
					new ActionListenerEx() {
						@Override
						public void actionPerformed() throws Exception {
							clear();
							setStatus(STATE_RUNNING);
						}
					}),
					l, t, w, h
					);
			t += h + yg;

			lo.add(createButton(
					"End",
					new ActionListenerEx() {
						@Override
						public void actionPerformed() throws Exception {
							end();
							setStatus(STATE_NOT_RUNNING);
						}
					}),
					l, t, w, h
					);
			t += h + yg;

			lo.add(_dlgStatus = new JLabel(STATE_READY, SwingConstants.CENTER),
					l, t, w, h
					);
			t += h + yg;

			lo.add(createButton(
					"Exit",
					new ActionListenerEx() {
						@Override
						public void actionPerformed() throws Exception {
							dlg.setVisible(false);
						}
					}),
					l, t, w, h
					);
			//t += h + yg;

			Container cp = dlg.getContentPane();

			cp.setLayout(new BorderLayout());
			cp.add(p, BorderLayout.CENTER);
		}

		dlg.setLocation(0, 0);
		setSize(dlg, 200, 300);
		dlg.setModal(true);
		dlg.setTitle("bluetears");

		start();
		setStatus(STATE_RUNNING);

		dlg.setVisible(true);
		dlg.dispose();
		frm.dispose();

		end();
	}

	private JButton createButton(String title, ActionListenerEx al) {
		JButton btn = new JButton(title);
		btn.addActionListener(al.getActionListener());
		return btn;
	}

	private void setSize(Component comp, int w, int h) {
		comp.setSize(w, h);
		comp.setMinimumSize(comp.getSize());
		//comp.setMaximumSize(comp.getSize());
	}

	private HttAlcott _ha = null; // 生存確認用
	private Thread _th;

	private void start() {
		if(_ha != null) {
			end();
		}
		_ha = new HttAlcott();
		_th = new Thread() {
			@Override
			public void run() {
				try {
					HttServer.perform(_ha);
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};
		_th.start();
	}

	private void clear() {
		if(_ha == null) {
			start();
		}
		_ha.clear();
	}

	private void end() {
		if(_ha == null) {
			return; // not running
		}
		_ha.end();

		ThreadTools.join(_th);

		_ha = null;
		_th = null;
	}

	private static String STATE_READY = "----";
	private static String STATE_RUNNING = "State = Running";
	private static String STATE_NOT_RUNNING = "State = Idling";

	private void setStatus(String state) {
		_frmStatus.setText(state);
		_frmStatus.repaint();
		_dlgStatus.setText(state);
	}
}
