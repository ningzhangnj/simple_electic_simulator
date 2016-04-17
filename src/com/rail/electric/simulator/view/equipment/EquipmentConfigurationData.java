package com.rail.electric.simulator.view.equipment;

public class EquipmentConfigurationData {
	private String name;
	private String oldValue;
	private String newValue;
	private String unit;
	private String range;
	private String resolution;
	
	public EquipmentConfigurationData(String name, String oldValue,
			String newValue, String range, String unit,  String resolution) {
		super();
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.range = range;
		this.unit = unit;		
		this.resolution = resolution;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	
	

}
