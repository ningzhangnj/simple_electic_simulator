package com.rail.electric.simulator.view.equipment;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class EquipmentConfigurationsTableLabelProvider 
	extends LabelProvider 
	implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int index) {
		EquipmentConfigurationData configData = (EquipmentConfigurationData) element;
		switch (index) {
			case 0 :
				return configData.getName();
			case 1 :
				return configData.getOldValue();
			case 2 :
				return configData.getNewValue();
			case 3 :
				return configData.getUnit();
			case 4:
				return configData.getRange();
			case 5:
				return configData.getResolution();
			default :
				break;
		}
		return "";
	}
}
