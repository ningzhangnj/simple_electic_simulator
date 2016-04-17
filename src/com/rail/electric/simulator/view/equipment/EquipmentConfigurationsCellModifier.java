package com.rail.electric.simulator.view.equipment;

import static com.rail.electric.simulator.view.EquipmentConfigurationsView.NAME;
import static com.rail.electric.simulator.view.EquipmentConfigurationsView.NEW_VALUE;
import static com.rail.electric.simulator.view.EquipmentConfigurationsView.OLD_VALUE;
import static com.rail.electric.simulator.view.EquipmentConfigurationsView.RANGE;
import static com.rail.electric.simulator.view.EquipmentConfigurationsView.RESOLUTION;
import static com.rail.electric.simulator.view.EquipmentConfigurationsView.UNIT;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

public class EquipmentConfigurationsCellModifier implements ICellModifier {
	private Viewer viewer;

	public EquipmentConfigurationsCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	public boolean canModify(Object element, String property) {
		switch (property) {
			case NEW_VALUE:
				return true;
			default :
				break;
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		EquipmentConfigurationData configData = (EquipmentConfigurationData) element;
		switch (property) {
			case NAME:
				return configData.getName();
			case OLD_VALUE:
				return configData.getOldValue();
			case NEW_VALUE:
				return configData.getNewValue();
			case UNIT:
				return configData.getUnit();
			case RANGE:
				return configData.getRange();
			case RESOLUTION:
				return configData.getResolution();
			default :
				break;
		}
		return "";
	}
	
	public void modify(Object element, String property, Object value) {
	    if (element instanceof Item) element = ((Item) element).getData();

	    EquipmentConfigurationData p = (EquipmentConfigurationData) element;
	    switch (property) {
		case NAME:
			p.setName((String)value);
			break;
		case OLD_VALUE:
			p.setOldValue((String)value);
			break;
		case NEW_VALUE:
			p.setNewValue((String)value);
			break;
		case UNIT:
			p.setUnit((String)value);
			break;
		case RANGE:
			p.setRange((String)value);
			break;
		case RESOLUTION:
			p.setResolution((String)value);
			break;
		default :
			break;
	}

	    viewer.refresh();
	  }
}