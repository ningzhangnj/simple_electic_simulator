package com.rail.electric.simulator.view.equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentConfigurations {
	private String name;
	private List<EquipmentConfigurationData> configurationDatas = new ArrayList<>();
	
	public EquipmentConfigurations(String name) {
		this.name = name;		
	}

	public String getName() {
		return name;
	}
	
	public void addConfigurationData(EquipmentConfigurationData configurationData) {
		configurationDatas.add(configurationData);
	}

	public List<EquipmentConfigurationData> getConfigurationData() {
		return configurationDatas;
	}
	
	

}
