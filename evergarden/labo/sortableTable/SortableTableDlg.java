package evergarden.labo.sortableTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import charlotte.tools.AnchoredLayout;

public class SortableTableDlg extends JDialog {
	private JTextField _keyword;
	private JButton _shiborikomiBtn;
	private JScrollPane _resultScroll;

	public static SortableTableDlg self = null;

	public SortableTableDlg(Frame owner) {
		super(owner);

		self = this;

		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				drawGroupingFrame(this, g, 10, 20, -10, 65, Color.GRAY, 20, 90);
			}
		};

		AnchoredLayout layout = new AnchoredLayout(panel);
		panel.setLayout(layout);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);

		layout.add(
				createLabel("検索条件", JLabel.LEFT),
				40,
				6,
				70,
				26,
				true,
				true,
				false,
				false
				);

		{
			int l = 20;
			int t = 40;
			int h = 26;
			int w;

			w = 80;
			layout.add(
					createLabel("キーワード :", JLabel.RIGHT),
					l,
					t,
					w,
					h,
					true,
					true,
					false,
					false
					);
			l += w + 10;

			w = -130;
			layout.add(
					_keyword = createTextField("(キーワード)"),
					l,
					t,
					w,
					h,
					true,
					true,
					true,
					false
					);
			l = w + 10;

			w = 100;
			layout.add(
					_shiborikomiBtn = createButton("絞り込み"),
					l,
					t,
					w,
					h,
					false,
					true,
					true,
					false
					);
			l += w;
		}

		layout.add(
				_resultScroll = createScroll(),
				10,
				95,
				-10,
				-10,
				true,
				true,
				true,
				true
				);

		this.setTitle("ソートできるテーブル");
		this.setSize(800, 600);
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(owner);
		this.setModal(true);

		// ----

		setToResultScroll(null);
		setToResultScroll(new SortableTable(Main.titles, Main.rows));
	}

	private SortableTable _result = null;

	private void setToResultScroll(SortableTable result) {
		_result = result;

		JTable table = result;

		if(table == null) {
			table = new JTable();
		}
		_resultScroll.getViewport().removeAll();
		_resultScroll.getViewport().add(table);
	}

	public void perform() {
		this.setVisible(true);
	}

	private JLabel createLabel(String title, int align) {
		JLabel ret = new JLabel(title);

		ret.setHorizontalAlignment(align);
		ret.setVerticalAlignment(JLabel.CENTER);

		return ret;
	}

	private JTextField createTextField(String placeHolder) {
		JTextField ret = new JTextField() {
			@Override
		    protected void paintComponent(final Graphics gg) {
				super.paintComponent(gg);

				if(this.getText().length() == 0) {
					Graphics2D g = (Graphics2D)gg;

					/*
					g.setRenderingHint(
							RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON
							);
							*/
					g.setColor(Color.LIGHT_GRAY);
					g.drawString(
							placeHolder,
							5,
							16
							);
				}
			}
		};

		return ret;
	}

	private JButton createButton(String title) {
		JButton ret = new JButton();

		ret.setText(title);

		return ret;
	}

	private JScrollPane createScroll() {
		JScrollPane ret = new JScrollPane();

		ret.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ret.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return ret;
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
}
