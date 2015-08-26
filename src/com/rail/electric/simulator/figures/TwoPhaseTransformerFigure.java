package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class TwoPhaseTransformerFigure extends BaseFigure {
	public static final Image LARGE_TRANSFORMER = new Image(Display.getCurrent(),			
			TwoPhaseTransformerFigure.class.getResourceAsStream("icons/TwoPhase_36X48.png"));	
	
	public static final int WIDTH = 36;
	public static final int HEIGHT = 48;
	
	public TwoPhaseTransformerFigure(int id, int x, int y) {
		super(id, LARGE_TRANSFORMER, x, y, WIDTH, HEIGHT);		
	}
}
