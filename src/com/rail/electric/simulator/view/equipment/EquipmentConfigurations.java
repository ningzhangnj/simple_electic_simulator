package com.rail.electric.simulator.view.equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentConfigurations {
	private String name;
	private String path;
	private List<EquipmentConfigurationData> configurationDatas = new ArrayList<>();
	
	public EquipmentConfigurations(String name, String path) {
		this.name = name;	
		this.path = path;
	}

	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}

	public void addConfigurationData(EquipmentConfigurationData configurationData) {
		configurationDatas.add(configurationData);
	}

	public List<EquipmentConfigurationData> getConfigurationData() {
		return configurationDatas;
	}

	public void setConfigurationDatas(
			List<EquipmentConfigurationData> configurationDatas) {
		this.configurationDatas = configurationDatas;
	}

}
