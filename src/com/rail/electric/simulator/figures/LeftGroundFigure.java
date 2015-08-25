package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class LeftGroundFigure extends StateFigure {
	public static final Image LARGE_GROUND_ON = new Image(Display.getCurrent(),			
			LeftGroundFigure.class.getResourceAsStream("icons/left_ground_on_48X64.png"));	
	public static final Image LARGE_GROUND_OFF = new Image(Display.getCurrent(),			
			LeftGroundFigure.class.getResourceAsStream("icons/left_ground_off_48X64.png"));
	public static final int WIDTH = 64;
	public static final int HEIGHT = 48;
	
	public LeftGroundFigure(int id,  String label, int x, int y) {
		super(id,  label,LARGE_GROUND_OFF, LARGE_GROUND_ON, x, y, WIDTH, HEIGHT);	
		this.setOn(false);
	}
}
