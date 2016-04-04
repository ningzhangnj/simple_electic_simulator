package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class CommunicationStateFigure extends ClickableFigure {
	public static final Image COMMUNICATION_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/communication_200X180.jpg"));
	
	public CommunicationStateFigure(int id, int x, int y) {
		super(id, COMMUNICATION_IMAGE, x, y, 200, 180);	
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
