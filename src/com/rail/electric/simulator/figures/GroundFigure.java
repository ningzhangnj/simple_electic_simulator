package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class GroundFigure extends StateFigure {
	public static final Image LARGE_GROUND_ON = new Image(Display.getCurrent(),			
			GroundFigure.class.getResourceAsStream("icons/ground_on_48X64.png"));	
	public static final Image LARGE_GROUND_OFF = new Image(Display.getCurrent(),			
			GroundFigure.class.getResourceAsStream("icons/ground_off_48X64.png"));
	public static final int WIDTH = 48;
	public static final int HEIGHT = 64;
	
	public GroundFigure(int id, String label, int x, int y,int initPower) {
		super(id, label, LARGE_GROUND_OFF, LARGE_GROUND_ON, x, y, WIDTH, HEIGHT, initPower);	
		this.setOn(false);
	}
}
