package com.rail.electric.simulator.model;

import com.rail.electric.simulator.SimulatorMessages;

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
		STUDENT_TEACHER_SIMULATOR(0, SimulatorMessages.Mode_StudentTeacherSimulator_Item),
		STUDENT_TEACHER(1, SimulatorMessages.Mode_StudentTeacher_Item),
		TEACHER_SIMULATOR(2, SimulatorMessages.Mode_TeacherSimulator_Item);
		
		private int index;
		private String label;
		
		private WorkMode(int index, String label) {
			this.index = index;
			this.label = label;
		}

		public int getIndex() {
			return index;
		}

		public String getLabel() {
			return label;
		}
		
		public static WorkMode getWorkMode(int index) {
			for (WorkMode mode : WorkMode.values()) {
				if (mode.getIndex() == index) return mode;
			}
			return null;
		}
		
	}
}
