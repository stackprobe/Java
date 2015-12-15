package charlotteFx.graph3;

import java.awt.Color;

public abstract class IChart {
	private Color _color;

	public IChart(Color color) {
		_color = color;
	}

	public Color getColor() {
		return _color;
	}

	public abstract double getValue(long fxTime);
}
