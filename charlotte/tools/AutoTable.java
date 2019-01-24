package charlotte.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoTable<T> {
	private List<List<T>> _rows = new ArrayList<List<T>>();
	private int _w;
	private int _h;

	public AutoTable() {
	}

	public AutoTable(int w, int h) {
		_w = w;
		_h = h;
	}

	public AutoTable(int w, int h, T pad) {
		_w = w;
		_h = h;
		padding(pad);
	}

	public int getWidth(){
		return _w;
	}

	public int getHeight() {
		return _h;
	}

	public void setWidth(int w) {
		_w = w;
	}

	public void setHeight(int h) {
		_h = h;
	}

	private void touch(int x, int y) {
		while(_rows.size() <= y) {
			_rows.add(new ArrayList<T>());
		}
		List<T> row = _rows.get(y);

		while(row.size() <= x) {
			row.add(null);
		}
	}

	public void set(int x, int y, T value) {
		touch(x, y);
		_rows.get(y).set(x, value);
		_w = Math.max(_w, x + 1);
		_h = Math.max(_h, y + 1);
	}

	public T get(int x, int y) {
		touch(x, y);
		return _rows.get(y).get(x);
	}

	public void clear() {
		_rows.clear();
		_w = 0;
		_h = 0;
	}

	public void twist() {
		AutoTable<T> dest = new AutoTable<T>();

		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				dest.set(y, x, get(x, y));
			}
		}
		capture(dest);
	}

	private void capture(AutoTable<T> target) {
		_rows = target._rows;
		_w = target._w;
		_h = target._h;
	}

	public void mirror() {
		for(List<T> row : _rows) {
			while(row.size() < _w) {
				row.add(null);
			}
			ArrayTools.reverse(row);
		}
	}

	public void reverse() {
		ArrayTools.reverse(_rows);
	}

	public void rotate90() {
		reverse();
		twist();
	}

	public void rotate180() {
		mirror();
		reverse();
	}

	public void rotate270() {
		twist();
		reverse();
	}

	public void rotateH90() {
		rotate270();
	}

	public void rotateH180() {
		rotate180();
	}

	public void rotateH270() {
		rotate90();
	}

	public void padding(T pad) {
		for(int x = 0; x < _w; x++) {
			for(int y = 0; y < _h; y++) {
				if(get(x, y) == null) {
					set(x, y, pad);
				}
			}
		}
	}

	// ----

	/**
	 * clear()
	 * -> newRow() -> addCell()...
	 * -> newRow() -> addCell()...
	 * -> newRow() -> addCell()...
	 * ...
	 */

	public void newRow() {
		_rows.add(new ArrayList<T>());
	}

	public void addCell(T cell) {
		set(_rows.get(_rows.size() - 1).size(), _rows.size() - 1, cell);
	}

	// ----

	public void addRow(T[] row) {
		addRow(Arrays.asList(row));
	}

	public void addRow(List<T> row) {
		newRow();

		for(T cell : row) {
			addCell(cell);
		}
	}

	public void addRows(T[][] rows) {
		for(T[] row : rows) {
			addRow(row);
		}
	}

	public void addRows(List<List<T>> rows) {
		for(List<T> row : rows) {
			addRow(row);
		}
	}

	public List<T> getRow(int y) {
		List<T> ret = new ArrayList<T>();

		for(int x = 0; x < _w; x++) {
			ret.add(get(x, y));
		}
		return ret;
	}

	public List<List<T>> getRows() {
		List<List<T>> ret = new ArrayList<List<T>>();

		for(int y = 0; y < _h; y++) {
			ret.add(getRow(y));
		}
		return ret;
	}

	public List<T> getColumn(int x) {
		List<T> ret = new ArrayList<T>();

		for(int y = 0; y < _h; y++) {
			ret.add(get(x, y));
		}
		return ret;
	}

	public List<List<T>> getColumns() {
		List<List<T>> ret = new ArrayList<List<T>>();

		for(int x = 0; x < _w; x++) {
			ret.add(getColumn(x));
		}
		return ret;
	}

	public static <T> AutoTable<T> create(T[][] rows) {
		AutoTable<T> ret = new AutoTable<T>();
		ret.addRows(rows);
		return ret;
	}

	public static <T> AutoTable<T> create(T[][] rows, T pad) {
		AutoTable<T> ret = new AutoTable<T>();
		ret.addRows(rows);
		ret.padding(pad);
		return ret;
	}

	public static <T> AutoTable<T> create(List<List<T>> rows) {
		AutoTable<T> ret = new AutoTable<T>();
		ret.addRows(rows);
		return ret;
	}

	public static <T> AutoTable<T> create(List<List<T>> rows, T pad) {
		AutoTable<T> ret = new AutoTable<T>();
		ret.addRows(rows);
		ret.padding(pad);
		return ret;
	}
}
