package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FaultRecordFigure extends ClickableFigure {
	public static final Image FAULT_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/fault_200X180.jpg"));
	
	public FaultRecordFigure(int id, int x, int y) {
		super(id, FAULT_IMAGE, x, y, 200, 180);
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
