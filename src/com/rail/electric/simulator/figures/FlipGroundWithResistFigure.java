package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FlipGroundWithResistFigure extends BaseFigure {
	public static final Image LARGE_GROUND_RESIST = new Image(Display.getCurrent(),			
			FlipGroundWithResistFigure.class.getResourceAsStream("icons/flip_ground_resist_32X64.png"));	
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 64;
	
	public FlipGroundWithResistFigure(int id, int x, int y) {
		super(id, LARGE_GROUND_RESIST, x, y, WIDTH, HEIGHT);		
	}
}
