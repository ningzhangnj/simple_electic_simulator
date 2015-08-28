package com.rail.electric.simulator.model;

public class StateModel {
	private int id;
	private boolean on;	
	
	public StateModel(int id, boolean on) {
		super();
		this.id = id;
		this.on = on;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	
	
}
