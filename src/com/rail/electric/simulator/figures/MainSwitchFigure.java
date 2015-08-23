package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class MainSwitchFigure extends StateFigure {
	public static final Image LARGE_SWITCH_ON = new Image(Display.getCurrent(),			
			MainSwitchFigure.class.getResourceAsStream("icons/main_switch_on_48X48.png"));	
	public static final Image LARGE_SWITCH_OFF = new Image(Display.getCurrent(),			
			MainSwitchFigure.class.getResourceAsStream("icons/main_switch_off_48X48.png"));
	public static final int WIDTH = 48;
	public static final int HEIGHT = 48;
	
	public MainSwitchFigure(int id, int x, int y) {
		super(id, LARGE_SWITCH_OFF, LARGE_SWITCH_ON, x, y, WIDTH, HEIGHT);		
	}
}
