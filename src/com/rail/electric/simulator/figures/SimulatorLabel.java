package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;

public class SimulatorLabel extends Label {
	public SimulatorLabel(String text, int x, int y) {
		super(text);
		setSize(60, 12);
		setLocation(new Point(x, y-100));
	}
	
	public SimulatorLabel(String text, int x, int y, int width, int height) {
		super(text);
		setSize(width, height);
		setLocation(new Point(x, y-100));
	}
}
