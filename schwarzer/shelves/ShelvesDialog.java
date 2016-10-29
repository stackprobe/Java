package schwarzer.shelves;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import charlotte.tools.IRect;
import charlotte.tools.ReflecTools;

public class ShelvesDialog extends JDialog {
	private ShelvesManager mgr;

	public ShelvesDialog(Frame parent, ShelvesManager mgr) {
		super(parent);
		this.mgr = mgr;
		init();
	}

	private void init() {
		Form form = mgr.getForm();

		form.outernal = new IRect(0, 0, form.width, form.height);
		form.internal = form.outernal.unextend(
				form.marginL,
				form.marginT,
				form.marginR,
				form.marginB
				);
		form.tabRect = form.internal.getClone();

		if(form.header != null) {
			Header header = form.header;

			header.outernal = new IRect(
					form.tabRect.l,
					form.tabRect.t,
					form.tabRect.w,
					header.height
					);
			header.internal = header.outernal.unextend(
					header.marginL,
					header.marginT,
					header.marginR,
					header.marginB
					);
			header.parent = form;
			header.index = 0;

			checkHeaderButtons(header);

			form.tabRect = form.tabRect.unextend(0, header.height, 0, 0);
		}
		if(form.footer != null) {
			Header footer = form.footer;

			footer.outernal = new IRect(
					form.tabRect.l,
					form.tabRect.getB() - footer.height,
					form.tabRect.w,
					footer.height
					);
			footer.internal = footer.outernal.unextend(
					footer.marginL,
					footer.marginT,
					footer.marginR,
					footer.marginB
					);
			footer.parent = form;
			footer.index = 1;

			checkHeaderButtons(footer);

			form.tabRect = form.tabRect.unextend(0, 0, 0, footer.height);
		}
		for(int tabIndex = 0; tabIndex < form.tabs.size(); tabIndex++) {
			Tab tab = form.tabs.get(tabIndex);

			tab.parent = form;
			tab.index = tabIndex;

			int lOrig = tab.marginL;
			int l = lOrig;
			int maxH = -1;

			for(int colIndex = 0; colIndex < tab.columns.size(); colIndex++) {
				Column column = tab.columns.get(colIndex);

				if(1 <= colIndex) {
					l += tab.colSpan;
				}
				column.rect = new IRect(
						l,
						tab.marginT,
						column.width,
						-1
						);
				column.parent = tab;
				column.index = colIndex;

				int tOrig = column.rect.t;
				int t = tOrig;

				for(int shelfIndex = 0; shelfIndex < column.shelves.size(); shelfIndex++) {
					Shelf shelf = column.shelves.get(shelfIndex);

					if(1 <= shelfIndex) {
						t += column.shelfSpan;
					}
					shelf.rect = new IRect(
							column.rect.l,
							t,
							column.rect.w,
							shelf.height
							);
					shelf.parent = column;
					shelf.index = shelfIndex;

					t += shelf.rect.h;
				}
				column.rect.h = t - tOrig;
				l += column.rect.w;
				maxH = Math.max(maxH, t - tOrig);
			}
			tab.internal = new IRect(
					tab.marginL,
					tab.marginT,
					l - lOrig,
					maxH
					);
			tab.outernal = tab.internal.extend(
					tab.marginL,
					tab.marginT,
					tab.marginR,
					tab.marginB
					);
		}

		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};

		AncLayoutMgr layout = new AncLayoutMgr(panel, form.outernal.w, form.outernal.h);
		panel.setLayout(layout);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);

		if(form.header != null) {
			addHeaderButtonsToLayout(layout, form.header, true);
		}
		if(form.footer != null) {
			addHeaderButtonsToLayout(layout, form.footer, false);
		}
		addTabsToLayout(layout, form);

		mgr.load();

		this.setTitle(form.title);
		this.setSize(form.outernal.w, form.outernal.h);
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(this.getOwner());
		this.setModal(true);
	}

	private void addTabsToLayout(AncLayoutMgr dlgLayout, Form form) {
		for(Tab tab : form.tabs) {
			JPanel panel = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
				}
			};

			AncLayoutMgr layout = new AncLayoutMgr(panel, tab.outernal.w, tab.outernal.h);
			panel.setLayout(layout);

			for(Column column : tab.columns) {
				for(Shelf shelf : column.shelves) {
					Component shelfComp = shelf.getComponent();

					layout.add(
							shelfComp,
							shelf.rect.l,
							shelf.rect.t,
							shelf.rect.w,
							shelf.rect.h,
							true,
							true,
							false,
							false
							);
				}
			}
			JScrollPane scroll = new JScrollPane();

			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll.getVerticalScrollBar().setUnitIncrement(25);
			scroll.getViewport().add(panel);

			tab.scroll = scroll;
		}
		dlgLayout.add(
				getTabComponent(form),
				form.tabRect.l,
				form.tabRect.t,
				form.tabRect.w,
				form.tabRect.h,
				true,
				true,
				true,
				true
				);

		for(Tab tab : form.tabs) {
			tab.scroll = null;
		}
	}

	private Component getTabComponent(Form form) {
		if(form.noTab) {
			return form.tabs.get(0).scroll;
		}

		{
			JTabbedPane comp = new JTabbedPane();

			for(Tab tab : form.tabs) {
				comp.add(tab.title, tab.scroll);
			}
			return comp;
		}
	}

	private void addHeaderButtonsToLayout(AncLayoutMgr layout, Header header, boolean alignTop) {
		for(Button button : header.buttons) {
			JButton comp = new JButton();

			comp.setText(button.title);
			comp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						call(button.method, button);
					}
					catch(Throwable ex) {
						ex.printStackTrace();
					}
				}
			});

			layout.add(
					comp,
					button.rect.l,
					button.rect.t,
					button.rect.w,
					button.rect.h,
					header.alignLeft(),
					alignTop,
					header.alignRight(),
					alignTop == false
					);
		}
	}

	private void checkHeaderButtons(Header header) {
		int lOrig = header.internal.l;
		int l = lOrig;

		for(int btnIndex = 0; btnIndex < header.buttons.size(); btnIndex++) {
			Button button = header.buttons.get(btnIndex);

			if(1 <= btnIndex) {
				l += header.buttonSpan;
			}
			button.rect = new IRect(
					l,
					header.internal.t,
					button.width,
					header.internal.h
					);
			button.parent = header;
			button.index = btnIndex;

			l += button.rect.w;
		}
		int m = header.internal.w - (l - lOrig);

		if(header.alignLeft()) {
			m = 0;
		}
		else if(header.alignMiddle()) {
			m /= 2;
		}
		for(Button button : header.buttons) {
			button.rect.l += m;
		}
	}

	private void call(String method, Button source) throws Exception {
		ReflecTools.invokeDeclaredMethod(mgr.getClass(), method, mgr, new Object[0]);
	}

	public void perform() {
		this.setVisible(true);
	}

	private void drawGroupingFrame(Component target, Graphics g, int l, int t, int w, int h, Color color, int label_l, int label_r) {
		if(l < 0) {
			l += target.getWidth();
		}
		if(t < 0) {
			t += target.getHeight();
		}
		if(w <= 0) {
			w += target.getWidth() - l;
		}
		if(h <= 0) {
			h += target.getHeight() - t;
		}
		label_l += l;
		label_r += l;

		g.setColor(color);
		g.drawLine(
				l,
				t,
				label_l,
				t
				);
		g.drawLine(
				label_r,
				t,
				l + w,
				t
				);
		g.drawLine(
				l,
				t,
				l,
				t + h
				);
		g.drawLine(
				l,
				t + h,
				l + w,
				t + h
				);
		g.drawLine(
				l + w,
				t,
				l + w,
				t + h
				);
	}

	public static class AncLayoutMgr implements LayoutManager {
		private Container _parent;
		private int _w;
		private int _h;

		public AncLayoutMgr(Container parent) {
			this(parent, 1000, 1000);
		}

		public AncLayoutMgr(Container parent, int w, int h) {
			_parent = parent;
			_w = w;
			_h = h;
		}

		private class ComponentInfo {
			public Component comp;
			public int l;
			public int t;
			public int w;
			public int h;
			public boolean anchorL;
			public boolean anchorT;
			public boolean anchorR;
			public boolean anchorB;
		}

		private List<ComponentInfo> _children = new ArrayList<ComponentInfo>();

		public void add(
				Component comp,
				int l, int t, int w, int h,
				boolean anchorL, boolean anchorT, boolean anchorR, boolean anchorB
				) {
			if(l < 0) {
				l += _w;
			}
			if(t < 0) {
				t += _h;
			}
			if(w <= 0) {
				w += _w - l;
			}
			if(h <= 0) {
				h += _h - t;
			}

			ComponentInfo ci = new ComponentInfo();

			ci.comp = comp;
			ci.l = l;
			ci.t = t;
			ci.w = w;
			ci.h = h;
			ci.anchorL = anchorL;
			ci.anchorT = anchorT;
			ci.anchorR = anchorR;
			ci.anchorB = anchorB;

			_parent.add(comp);
			_children.add(ci);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// ignore
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			// ignore
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(_w, _h);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
			//return new Dimension(_w, _h);
		}

		@Override
		public void layoutContainer(Container parent) {
			int currW = parent.getWidth();
			int currH = parent.getHeight();

			int diffX = currW - _w;
			int diffY = currH - _h;

			for(ComponentInfo child : _children) {
				double addL = 0.0;
				double addT = 0.0;
				double addW = 0.0;
				double addH = 0.0;

				switch((child.anchorL ? 1 : 0) | (child.anchorR ? 2 : 0)) {
				case 0: addL = 0.5; addW = 0.0; break;
				case 1: addL = 0.0; addW = 0.0; break;
				case 2: addL = 1.0; addW = 0.0; break;
				case 3: addL = 0.0; addW = 1.0; break;
				}

				switch((child.anchorT ? 1 : 0) | (child.anchorB ? 2 : 0)) {
				case 0: addT = 0.5; addH = 0.0; break;
				case 1: addT = 0.0; addH = 0.0; break;
				case 2: addT = 1.0; addH = 0.0; break;
				case 3: addT = 0.0; addH = 1.0; break;
				}

				child.comp.setLocation(
						child.l + (int)(addL * diffX),
						child.t + (int)(addT * diffY)
						);
				child.comp.setSize(new Dimension(
						child.w + (int)(addW * diffX),
						child.h + (int)(addH * diffY)
						));
			}
		}
	}
}
