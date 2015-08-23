package com.rail.electric.simulator.figures;

import java.util.List;

import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

public class LineFigure extends Polyline {
	private int id;
	private Color deadColor;
	private Color activeColor;
	private boolean isOn = false;
	private StateListener listener;
	

	public LineFigure(int id, List<Point> points, Color deadColor, Color activeColor, int width) {
		super();
		this.id = id;
		for (Point point : points) {
			this.addPoint(point);
		}
		this.deadColor = deadColor;
		this.activeColor = activeColor;
		this.setForegroundColor(deadColor);
		this.setLineWidth(width);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void activate() {
		this.setForegroundColor(activeColor);
	}
	
	public void deactivate() {
		this.setForegroundColor(deadColor);
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
		if (isOn) activate();
		else deactivate();
	}
	
	public void switchState() {
		setOn(!isOn);		
		
		if (listener != null) {
			listener.onChange(getId(), isOn);
		}
	}
	
	public void registerStateListener(StateListener listener) {
		this.listener = listener;
	}
}
