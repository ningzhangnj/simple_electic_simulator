package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class GroundTransformerFigure extends BaseFigure {
	public static final Image LARGE_TRANSFORMER = new Image(Display.getCurrent(),			
			GroundTransformerFigure.class.getResourceAsStream("icons/ground_transfer_40X64.png"));	
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 64;
	
	public GroundTransformerFigure(int id, int x, int y) {
		super(id, LARGE_TRANSFORMER, x, y, WIDTH, HEIGHT);		
	}
}
