package com.rail.electric.simulator.model;

public class StudentWorkstation extends AbstractBaseModel {
		
	public String getIp() {
		return getProperty("ip", "127.0.0.1");
	}
	
	public void setIp(String ip) {
		setProperty("ip", ip);
	}
	
	public int getPort() {
		return Integer.parseInt(getProperty("port", "9876"));
	}
	
	public void setPort(int port) {
		setProperty("port", Integer.toString(port));
	}
	
	@Override
	protected String getConfFileName() {
		return "student";
	}
	
}
