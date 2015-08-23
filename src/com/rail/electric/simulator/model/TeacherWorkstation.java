package com.rail.electric.simulator.model;

public class TeacherWorkstation {
	private int port;
	private WorkMode mode;
	private int quizNo;
	private String comPort;
	
	
	
	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public WorkMode getMode() {
		return mode;
	}



	public void setMode(WorkMode mode) {
		this.mode = mode;
	}



	public int getQuizNo() {
		return quizNo;
	}



	public void setQuizNo(int quizNo) {
		this.quizNo = quizNo;
	}



	public String getComPort() {
		return comPort;
	}



	public void setComPort(String comPort) {
		this.comPort = comPort;
	}



	public enum WorkMode {
		PROXY,
		RECV;		
	}
}
