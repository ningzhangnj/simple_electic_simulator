package com.rail.electric.simulator.model;

import com.rail.electric.simulator.SimulatorMessages;

public class TeacherWorkstation extends AbstractBaseModel {
		
	public int getPort() {
		return Integer.parseInt(getProperty("port", "9876"));
	}

	public void setPort(int port) {
		setProperty("port", Integer.toString(port));
	}
	
	public WorkMode getMode() {
		return WorkMode.valueOf(getProperty("mode", "STUDENT_TEACHER"));
	}
	
	public void setMode(WorkMode mode) {
		setProperty("mode", mode.name());
	}
		
	public String getComPort() {
		return getProperty("comPort", "COM1");
	}

	public void setComPort(String comPort) {
		setProperty("comPort", comPort);
	}

	@Override
	protected String getConfFileName() {
		return "teacher";
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
