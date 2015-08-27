package com.rail.electric.simulator.figures;

import java.util.List;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

public class PolyLineFigure extends LineFigure {

	public PolyLineFigure(int id, List<Point> points, Color deadColor,
			Color activeColor, int width, int initPower) {
		super(id, points, deadColor, activeColor, width, initPower);
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setLocation(points.get(points.size()-1));
		//decoration.setReferencePoint(points.get(points.size()-2));
		add(decoration);
	}

}
