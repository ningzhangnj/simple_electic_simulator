package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class LoadCurveFigure extends ClickableFigure {
	public static final Image LOAD_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/load_200X180.jpg"));
	
	public LoadCurveFigure(int id, int x, int y) {
		super(id, LOAD_IMAGE, x, y, 200, 180);	
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
