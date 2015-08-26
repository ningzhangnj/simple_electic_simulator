package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class SmallThreePhaseTransformerFigure extends BaseFigure {
	public static final Image LARGE_TRANSFORMER = new Image(Display.getCurrent(),			
			SmallThreePhaseTransformerFigure.class.getResourceAsStream("icons/three_phase_48X48.png"));	
	
	public static final int WIDTH = 48;
	public static final int HEIGHT = 48;
	
	public SmallThreePhaseTransformerFigure(int id, int x, int y) {
		super(id, LARGE_TRANSFORMER, x, y, WIDTH, HEIGHT);		
	}
}
