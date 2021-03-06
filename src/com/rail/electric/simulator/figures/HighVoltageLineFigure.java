package com.rail.electric.simulator.figures;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

public class HighVoltageLineFigure extends LineFigure {
	public HighVoltageLineFigure(int id, List<Point> points,int initPower) {
		super( id, points, 
				new Color(null, 0,160,0), 
				new Color(null, 240,0,0), 
				4, initPower);		
	}
}
