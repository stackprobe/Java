package evergarden.labo.grap2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;

import charlotte.tools.Bmp;
import charlotte.tools.SwingTools;

public class Test01 {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)bi.getGraphics();

		g.setColor(Color.BLUE);
		g.fillRect(0, 10, 100, 80);

		g.setColor(Color.ORANGE);
		g.drawLine(10, 20, 90, 80);

		g.setColor(Color.CYAN);
		g.drawLine(10, 10, 11, 10);
		g.drawLine(11, 10, 11, 12);
		g.drawLine(11, 12, 10, 12);
		g.drawLine(10, 12, 10, 14);
		g.drawLine(10, 14, 11, 14);

		g.setColor(Color.RED);
		g.drawLine(20, 10, 20, 10);

		Bmp bmp = Bmp.getBmp(bi);
		bmp.writeToFile("C:/temp/1.png");
	}

	private static JDialog _dlg;

	private static void test02Redraw() {
		Graphics2D g = (Graphics2D)_dlg.getGraphics();
		Rectangle size = new Rectangle();

		g.getClipBounds(size);

		System.out.println("size: " + size);

		g.setColor(Color.BLUE);
		//drawBox(g, 0, 0, 100, 100);
		//drawBox(g, 8, 30, 691, 491); // win7エアロ,700,500 でぴったり。

		{
			final int MGN_L = 8;
			final int MGN_T = 30;
			final int MGN_R = 8;
			final int MGN_B = 8;

			drawBox(g, MGN_L, MGN_T, _dlg.getWidth() - 1 - MGN_R, _dlg.getHeight() - 1 - MGN_B);
		}
	}

	private static void drawBox(Graphics2D g, int l, int t, int r, int b) {
		g.drawLine(l, t, r, t);
		g.drawLine(l, t, l, b);
		g.drawLine(r, t, r, b);
		g.drawLine(l, b, r, b);
		g.drawLine(l, t, r, b);
		g.drawLine(l, b, r, t);
	}

	private static Object T2RR_SYNCROOT = new Object();
	private static boolean _t2rrRedrawRequested = false;

	private static void test02RequestRedraw() {
		synchronized(T2RR_SYNCROOT) {
			if(_t2rrRedrawRequested == false) {
				SwingTools.invokeLaterDeep(new Runnable() {
					@Override
					public void run() {
						synchronized(T2RR_SYNCROOT) {
							_t2rrRedrawRequested = false;
						}
						test02Redraw();
					}
				}
				,0
				,100
				);
				_t2rrRedrawRequested = true;
			}
		}
	}

	private static void test02() {
		_dlg = new JDialog() {
			@Override
			public void repaint(long millis, int l, int t, int w, int h) {
				super.repaint(millis, l, t, w, h);
				System.out.println("repaint");
				test02RequestRedraw();
			}

			@Override
			public void update(Graphics g) {
				super.update(g);
				System.out.println("update");
				test02RequestRedraw();
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				System.out.println("paint");
				test02RequestRedraw();
			}
		};

		_dlg.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// noop
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					test02Redraw();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// noop
			}
		});

		_dlg.setSize(700, 500);
		_dlg.setModal(true);

		//test02Timer();
		test02RequestRedraw();

		_dlg.setVisible(true);
	}

	private static void test02Timer() {
		SwingTools.invokeLaterDeep(new Runnable() {
			@Override
			public void run() {
				test02Redraw();
				test02Timer();
			}
		}
		,0
		,3000
		);
	}
}
