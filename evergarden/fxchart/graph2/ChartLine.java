package evergarden.fxchart.graph2;

import java.awt.Color;

public abstract class ChartLine {
	private Color _color;

	public ChartLine(Color color) {
		_color = color;
	}

	public Color getColor() {
		return _color;
	}

	public abstract double getValue(long fxTime);

	public ChartLineSpan getSpan(long startFxTime, long endFxTime) {
		return new ChartLineSpan(this, startFxTime, endFxTime);
	}
}
