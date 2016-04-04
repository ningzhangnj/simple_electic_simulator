package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class CoverFigure extends BaseFigure {
	public static final Image LARGE_MAIN_COVER = new Image(Display.getCurrent(),			
			CoverFigure.class.getResourceAsStream("icons/cover_title.jpg"));	
	
	public static final int WIDTH = 1800;
	public static final int HEIGHT = 940;
	
	public CoverFigure(int id, int x, int y) {
		super(id, LARGE_MAIN_COVER, x, y, WIDTH, HEIGHT);		
	}
}
