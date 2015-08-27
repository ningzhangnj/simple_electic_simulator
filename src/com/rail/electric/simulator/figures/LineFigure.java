package com.rail.electric.simulator.figures;

import java.util.List;

import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import com.rail.electric.simulator.listeners.StateListener;

public class LineFigure extends Polyline {
	private int id;
	private Color deadColor;
	private Color activeColor;
	private int power; ////0 means no power, 1 means #1(1<<0), 2 means #2(1<<1)
	private StateListener listener;
	

	public LineFigure(int id, List<Point> points, Color deadColor, Color activeColor, int width, int initPower) {
		super();
		this.id = id;
		for (Point point : points) {
			point.y -= 100; 
			this.addPoint(point);
		}
		this.deadColor = deadColor;
		this.activeColor = activeColor;
		this.power = initPower;
		this.setForegroundColor(power>0?activeColor:deadColor);
		this.setLineWidth(width);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;

		this.setForegroundColor(power>0?activeColor:deadColor);
	}
	
	public void triggerPower(int power) {
		setPower(power);		
		
		if (listener != null) {
			listener.onChange(getId(), power);
		}
	}
	
	public void registerStateListener(StateListener listener) {
		this.listener = listener;
	}
}
