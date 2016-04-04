package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class EquipmentConfigurationsFigure extends ClickableFigure {
	public static final Image EQUIPMENT_IMAGE = new Image(Display.getCurrent(),			
			MainTransformerFigure.class.getResourceAsStream("icons/equipment_200X180.jpg"));
	
	public EquipmentConfigurationsFigure(int id, int x, int y) {
		super(id, EQUIPMENT_IMAGE, x, y, 200, 180);	
		this.setBorder(new SimpleRaisedBorder(3));
	}

}
