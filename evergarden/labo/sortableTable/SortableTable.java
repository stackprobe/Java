package evergarden.labo.sortableTable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import charlotte.tools.ArrayTools;
import charlotte.tools.DoubleTools;
import charlotte.tools.StringTools;

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

		public void setWidth(TableColumn column, int w) {
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
		boolean numericColumn = isNumericColumn(_orderColidx);

		ArrayTools.sort(_rows, new Comparator<String[]>() {
			@Override
			public int compare(String[] row1, String[] row2) {
				String cell1 = row1[_orderColidx];
				String cell2 = row2[_orderColidx];
				int ret;

				if(numericColumn) {
					double val1 = Double.parseDouble(cell1.replaceAll(",", ""));
					double val2 = Double.parseDouble(cell2.replaceAll(",", ""));

					ret = DoubleTools.comp.compare(val1, val2);
				}
				else {
					ret = StringTools.comp.compare(cell1, cell2);
				}
				return _orderAsc ? ret : -ret;
			}
		});
	}

	private boolean isNumericColumn(int colidx) {
		try {
			for(String[] row : _rows) {
				String cell = row[colidx];

				cell = cell.replace(",", "");

				Double.parseDouble(cell);
			}
			return true;
		}
		catch(Throwable e) {
			// ignore
		}
		return false;
	}

	private void doRepaint() {
		this.setModel(new ResultTableModel());
		SortableTableDlg.self.repaint();
	}
}
