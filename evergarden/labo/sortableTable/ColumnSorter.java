package evergarden.labo.sortableTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.DoubleTools;
import charlotte.tools.LongTools;
import charlotte.tools.StringTools;

public class ColumnSorter {
	private List<String[]> _rows;
	private int _colidx;
	private boolean _asc;

	public ColumnSorter(List<String[]> rows, int colidx, boolean asc) {
		_rows = rows;
		_colidx = colidx;
		_asc = asc;
	}

	public void perform() {
		List<RowInfo> rows = new ArrayList<RowInfo>();

		int rowidx = -1;
		for(String[] row : _rows) {
			rowidx++;

			RowInfo ri = new RowInfo();

			ri.rowidx = rowidx;
			ri.row = row;

			String cell = row[_colidx];

			ri.longCell = toLong(cell);
			ri.doubleCell = toDouble(cell);
			ri.cell = cell;

			rows.add(ri);
		}

		ArrayTools.sort(rows, new Comparator<RowInfo>() {
			@Override
			public int compare(RowInfo a, RowInfo b) {
				int ret = compareCell(a, b);

				if(ret != 0) {
					return _asc ? ret : -ret;
				}
				return a.rowidx - b.rowidx;
			}
		});

		_rows.clear();

		for(RowInfo ri : rows) {
			_rows.add(ri.row);
		}
	}

	private static class RowInfo {
		public int rowidx;
		public String[] row;
		public Long longCell;
		public Double doubleCell;
		public String cell;
	}

	private static int compareCell(RowInfo a, RowInfo b) {
		if(a.longCell != null && b.longCell != null) {
			return LongTools.comp.compare(a.longCell, b.longCell);
		}
		if(a.doubleCell != null && b.doubleCell != null) {
			return DoubleTools.comp.compare(a.doubleCell, b.doubleCell);
		}
		return StringTools.comp.compare(a.cell, b.cell);
	}

	private static Long toLong(String str) {
		try {
			str = StringTools.zenToHan(str);
			str = str.trim();
			str = str.replace(",", "");

			return Long.parseLong(str);
		}
		catch(Throwable e) {
			// ignore
		}
		return null;
	}

	private static Double toDouble(String str) {
		try {
			str = StringTools.zenToHan(str);
			str = str.trim();
			str = str.replace(",", "");

			return Double.parseDouble(str);
		}
		catch(Throwable e) {
			// ignore
		}
		return null;
	}
}
