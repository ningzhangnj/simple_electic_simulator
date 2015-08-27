package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FlipThreePahseTransformerFigure extends StateFigure {
	public static final Image LARGE_TRANSFORMER_ON = new Image(Display.getCurrent(),			
			FlipThreePahseTransformerFigure.class.getResourceAsStream("icons/flip_trip_trans_on_40X72.png"));	
	public static final Image LARGE_TRANSFORMER_OFF = new Image(Display.getCurrent(),			
			FlipThreePahseTransformerFigure.class.getResourceAsStream("icons/flip_trip_trans_off_40X72.png"));
	public static final int WIDTH = 40;
	public static final int HEIGHT = 72;
	
	public FlipThreePahseTransformerFigure(int id, String label, int x, int y,int initPower) {
		super(id, label, LARGE_TRANSFORMER_OFF, LARGE_TRANSFORMER_ON, x, y, WIDTH, HEIGHT, initPower);		
	}
}
