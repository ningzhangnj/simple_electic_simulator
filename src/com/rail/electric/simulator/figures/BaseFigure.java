package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

public abstract class BaseFigure extends Label {
	private int id;
	
	BaseFigure(int id, Image icon, int x, int y, int width, int height) {
		super(icon);
		this.id = id;
		setSize(width, height);
		setLocation(new Point(x, y));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
