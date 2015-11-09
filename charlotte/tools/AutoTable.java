package charlotte.tools;

import java.util.ArrayList;
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

	private void capture(AutoTable<T> target) {
		_rows = target._rows;
		_w = target._w;
		_h = target._h;
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

	public void mirror() {
		for(List<T> row : _rows) {
			ArrayTools.reverse(row);
		}
	}

	public void reverse() {
		ArrayTools.reverse(_rows);
	}

	public void rotate_ra1() {
		reverse();
		twist();
	}

	public void rotate_ra2() {
		mirror();
		reverse();
	}

	public void rotate_ra3() {
		twist();
		reverse();
	}

	public void rotate_ccw_ra1() {
		rotate_ra3();
	}

	public void rotate_ccw_ra2() {
		rotate_ra2();
	}

	public void rotate_ccw_ra3() {
		rotate_ra1();
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
}
