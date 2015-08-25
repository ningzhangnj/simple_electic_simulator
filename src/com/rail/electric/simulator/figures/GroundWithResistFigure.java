package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class GroundWithResistFigure extends BaseFigure {
	public static final Image LARGE_GROUND_RESIST = new Image(Display.getCurrent(),			
			GroundWithResistFigure.class.getResourceAsStream("icons/ground_resist_32X64.png"));	
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 64;
	
	public GroundWithResistFigure(int id, int x, int y) {
		super(id, LARGE_GROUND_RESIST, x, y, WIDTH, HEIGHT);		
	}
}
