package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ConnectionsEntryFigure extends ClickableFigure {
	public static final Image CONNECTIONS_ENTRY = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/double_trans_64X64.png"));
	
	public ConnectionsEntryFigure(int id, int x, int y) {
		super(id, CONNECTIONS_ENTRY, x, y, 64, 64);		
	}

}
