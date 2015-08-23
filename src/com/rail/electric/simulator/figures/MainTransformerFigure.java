package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class MainTransformerFigure extends BaseFigure {
	public static final Image LARGE_MAIN_TRANSFORMER = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/double_trans_64X64.png"));	
	
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	
	public MainTransformerFigure(int id, int x, int y) {
		super(id, LARGE_MAIN_TRANSFORMER, x, y, WIDTH, HEIGHT);		
	}
}
