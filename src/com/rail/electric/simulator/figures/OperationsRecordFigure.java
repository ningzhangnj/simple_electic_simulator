package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class OperationsRecordFigure extends ClickableFigure {
	public static final Image OPERATIONS_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/operation_200X180.jpg"));
	
	public OperationsRecordFigure(int id, int x, int y) {
		super(id, OPERATIONS_IMAGE, x, y, 200, 180);		
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
