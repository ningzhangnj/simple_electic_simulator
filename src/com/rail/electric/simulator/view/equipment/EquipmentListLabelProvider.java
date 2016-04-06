package com.rail.electric.simulator.view.equipment;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class EquipmentListLabelProvider extends LabelProvider {
		
	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		EquipmentConfigurations equip = (EquipmentConfigurations) element;
		return equip.getName();
	}
}
