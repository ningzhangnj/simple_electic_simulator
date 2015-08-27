package com.rail.electric.simulator.model;

public class StateModel {
	private int id;
	private boolean state;	
	
	public StateModel(int id, boolean state) {
		super();
		this.id = id;
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	
}
