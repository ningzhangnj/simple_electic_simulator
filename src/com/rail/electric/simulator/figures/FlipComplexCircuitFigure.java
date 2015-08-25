package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FlipComplexCircuitFigure extends StateFigure {
	public static final Image LARGE_COMPLEX_CIRCUIT_ON = new Image(Display.getCurrent(),			
			FlipComplexCircuitFigure.class.getResourceAsStream("icons/flip_comp_on_64X64.png"));
	public static final Image LARGE_COMPLEX_CIRCUIT_OFF = new Image(Display.getCurrent(),			
			FlipComplexCircuitFigure.class.getResourceAsStream("icons/flip_comp_off_64X64.png"));
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	
	public FlipComplexCircuitFigure(int id, String label, int x, int y) {
		super(id, label, LARGE_COMPLEX_CIRCUIT_OFF, LARGE_COMPLEX_CIRCUIT_ON, x, y, WIDTH, HEIGHT);		
	}
}
