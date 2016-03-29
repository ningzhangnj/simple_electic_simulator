package com.rail.electric.simulator.figures;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class EquipmentConfigurationsFigure extends ClickableFigure {
	public static final Image CONNECTIONS_ENTRY = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/connections_300X225.jpg"));
	
	public EquipmentConfigurationsFigure(int id, int x, int y) {
		super(id, CONNECTIONS_ENTRY, x, y, 200, 160);		
	}

}
