package evergarden.fxchart.graph;

import java.awt.Color;

public interface IChart {
	public Color getColor();
	public double getValue(long sec);
}
