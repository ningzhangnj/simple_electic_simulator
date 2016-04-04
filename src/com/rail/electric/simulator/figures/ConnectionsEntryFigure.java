package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ConnectionsEntryFigure extends ClickableFigure {
	public static final Image CONNECTIONS_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/connections_200X180.jpg"));
	
	public ConnectionsEntryFigure(int id, int x, int y) {
		super(id, CONNECTIONS_IMAGE, x, y, 200, 180);	
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
