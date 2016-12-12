package evergarden.labo.sortableTable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SortableTable extends JTable {
	private String[] _titles;
	private List<String[]> _rows;

	public SortableTable(String[] titles, List<String[]> rows) {
		_titles = titles;
		_rows = rows;

		this.setModel(new ResultTableModel());

		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.getTableHeader().setReorderingAllowed(false);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		new AutoResizeColumns(this).perform();

		this.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(isNearColumnBorder(e.getPoint())) {
					return;
				}
				int colidx = SortableTable.this.getTableHeader().columnAtPoint(e.getPoint());

				if(colidx == _orderColidx) {
					setOrderColumn(colidx, _orderAsc == false);
				}
				else {
					setOrderColumn(colidx, true);
				}
			}
		});
	}

	private static final int COLUMN_BORDER_MARGIN = 5;

	private boolean isNearColumnBorder(Point point) {
		Point l = new Point(point.x - COLUMN_BORDER_MARGIN, point.y);
		Point r = new Point(point.x + COLUMN_BORDER_MARGIN, point.y);

		int colidx = SortableTable.this.getTableHeader().columnAtPoint(point);
		int lColidx = SortableTable.this.getTableHeader().columnAtPoint(l);
		int rColidx = SortableTable.this.getTableHeader().columnAtPoint(r);

		return colidx != lColidx || colidx != rColidx;
	}

	private class ResultTableModel implements TableModel {
		@Override
		public void addTableModelListener(TableModelListener tml) {
			// noop
		}

		@Override
		public Class<?> getColumnClass(int colidx) {
			return JTextField.class;
		}

		@Override
		public int getColumnCount() {
			return _titles.length;
		}

		@Override
		public String getColumnName(int colidx) {
			String title = _titles[colidx];

			if(colidx == _orderColidx) {
				title += _orderAsc ? " ↑" : " ↓";
			}
			return title;
		}

		@Override
		public int getRowCount() {
			return _rows.size();
		}

		@Override
		public Object getValueAt(int rowidx, int colidx) {
			return _rows.get(rowidx)[colidx];
		}

		@Override
		public boolean isCellEditable(int rowidx, int colidx) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener tml) {
			// noop
		}

		@Override
		public void setValueAt(Object value, int rowidx, int colidx) {
			// noop
		}
	}

	private static class AutoResizeColumns {
		private JTable _table;

		public AutoResizeColumns(JTable table) {
			_table = table;
		}

		public void perform() {
			for(int colidx = 0; colidx < _table.getColumnCount(); colidx++) {
				int w = getPreWidth(_table.getColumnModel().getColumn(colidx).getHeaderValue(), -1, colidx);

				for(int rowidx = 0; rowidx < _table.getRowCount(); rowidx++) {
					w = Math.max(w, getPreWidth(_table.getValueAt(rowidx, colidx), rowidx, colidx));
				}
				if(w != -1) {
					setWidth(_table.getColumnModel().getColumn(colidx), w + 5);
				}
			}
		}

		private int getPreWidth(Object value, int rowidx, int colidx) {
			TableCellRenderer renderer = _table.getCellRenderer(rowidx, colidx);

			Component comp = renderer.getTableCellRendererComponent(
					_table,
					value,
					false,
					false,
					rowidx,
					colidx
					);

			if(comp instanceof JComponent) {
				JComponent jComp = (JComponent)comp;
				Dimension size = jComp.getPreferredSize();

				return size.width;
			}
			return -1;
		}

		private void setWidth(TableColumn column, int w) {
			column.setMinWidth(w);
			column.setWidth(w);
		}
	}

	private void resetRows(List<String[]> rows) {
		_rows = rows;
		doSort();
		doRepaint();
	}

	private void clearOrderColumn() {
		setOrderColumn(-1, false);
	}

	private int _orderColidx = -1;
	private boolean _orderAsc = false;

	private void setOrderColumn(int colidx, boolean asc) {
		_orderColidx = colidx;
		_orderAsc = asc;
		doSort();
		doRepaint();
	}

	private void doSort() {
		if(_orderColidx == -1) {
			return;
		}
		new ColumnSorter(_rows, _orderColidx, _orderAsc).perform();
	}

	private void doRepaint() {
		int[][] ws = getAllColumnWidth();
		this.setModel(new ResultTableModel());
		setAllColumnWidth(ws);

		SortableTableDlg.self.repaint();
	}

	private int[][] getAllColumnWidth() {
		TableColumnModel tcm = this.getColumnModel();
		int[][] ws = new int[_titles.length][];

		for(int colidx = 0; colidx < _titles.length; colidx++) {
			TableColumn tc = tcm.getColumn(colidx);

			ws[colidx] = new int[] {
					tc.getWidth(),
					tc.getMinWidth(),
			};
		}
		return ws;
	}

	private void setAllColumnWidth(int ws[][]) {
		TableColumnModel tcm = this.getColumnModel();

		for(int colidx = 0; colidx < _titles.length; colidx++) {
			TableColumn tc = tcm.getColumn(colidx);

			tc.setMinWidth(ws[colidx][0]);
			tc.setWidth(ws[colidx][0]);
			tc.setMinWidth(ws[colidx][1]);
		}
	}
}
