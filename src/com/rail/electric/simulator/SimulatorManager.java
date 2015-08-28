package com.rail.electric.simulator;

import static com.rail.electric.simulator.SimulatorFiguresCollections.SWITCH_OFFSET;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import org.eclipse.draw2d.FreeformLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.model.TeacherWorkstation.WorkMode;
import com.rail.electric.simulator.worker.StudentWorker;
import com.rail.electric.simulator.worker.TeacherWorker;

public class SimulatorManager {
	private final static Logger logger =  LoggerFactory.getLogger(SimulatorManager.class);
	
	public static final byte BEGIN_BYTE = (byte)0xf5;
	public static final byte END_BYTE = (byte)0xfa;
	public static final byte CORRECT_PACKET_BYTE = (byte)0xf6;
	public static final byte READ_SWITCH_BYTE = (byte)0xf4;
	
	public static final byte QUIZ_SUBJECT_HEAD_BYTE = (byte)0xf1;
	public static final byte QUIZ_INITSTATE_HEAD_BYTE = (byte)0xf2;
	public static final byte QUIZ_CORRECT_HEAD_BYTE = (byte)0xf7;
	public static final byte QUIZ_WRONG_HEAD_BYTE = (byte)0xf8;
	public static final byte SWITCH_STATUS_HEAD_BYTE = (byte)0xf9;
	
	private SimulatorFiguresCollections simModel;
	private WorkStatus status;
	private FreeformLayer primaryLayer;	
	
	private StudentWorker studentWorker;
	private TeacherWorker teacherWorker;
	
	private static  SimulatorManager instance;
	
	public static SimulatorManager getInstance(FreeformLayer layer,
			WorkStatus status) {
		if (instance == null) {
			instance = new SimulatorManager(layer, status);
		}
		return instance;
	}
	
	public static SimulatorManager getInstance() {
		return instance;
	}
	
	private SimulatorManager(FreeformLayer layer,
			WorkStatus status) {
		this.primaryLayer = layer;
		this.status = status;
		simModel = new SimulatorFiguresCollections(primaryLayer, this);
	}

	public void deactivate() {
		stop();
		simModel.deactivate();
	}

	public void importConnections(File file) {
		simModel.importConnections(file);
	}
	
	public void startTeacher(int port, WorkMode mode, String commPort) {
		simModel.init();
		simModel.activate();
		if (teacherWorker == null) {
			teacherWorker = new TeacherWorker(port, mode, commPort, this);
		}
		teacherWorker.start();
	}
	
	public void stopTeacher() {
		simModel.init();
		simModel.activate();
		if (teacherWorker != null) {
			teacherWorker.stop();
		}
	}
	
	public void startStudent(String ip, int port) {
		if (studentWorker == null) {
			studentWorker = new StudentWorker(ip, port, this);
		}
		studentWorker.start();
	}
	
	public void stopStudent() {
		if (studentWorker != null) {
			studentWorker.stop();
		}
	}
	
	public void stop() {
		if (status == WorkStatus.RUNNING_STUDENT) stopStudent();
		else if (status == WorkStatus.RUNNING_TEACHER) stopTeacher();
	}
	
	public void updateSwitchStatus(byte[] switchStatusBytes) {
		simModel.updateSwitchStatus(switchStatusBytes);
	}
	
	public void updateInitState(byte[] initStateBytes) {
		simModel.updatInitState(initStateBytes);
	}
	
	public void writeInitStateBytes(DataOutputStream output) {
		simModel.writeInitStateBytes(output, QUIZ_INITSTATE_HEAD_BYTE);
	}
	
	public void writeQuizNameBytes(DataOutputStream output) {
		simModel.writeInitStateBytes(output, QUIZ_SUBJECT_HEAD_BYTE);
	}
	
	public boolean validate(int id, boolean state) {
		if (status == WorkStatus.RUNNING_STUDENT) {
			ByteBuffer bb = ByteBuffer.allocate(7);
			bb.putInt(3);
			bb.put(4, SWITCH_STATUS_HEAD_BYTE);
			bb.put(5, (byte)(id - SWITCH_OFFSET));
			bb.put(6, state?(byte)1:(byte)0);
			return studentWorker.sendMessageAndWaitBooleanResult(bb.array());
		}
		else if (status == WorkStatus.RUNNING_TEACHER) return true;
		return true;
	}
	
	public void validateAndUpdateSwitchStatus(byte[] switchStatusBytes) {
		if (simModel.validate(switchStatusBytes)) {
			simModel.updateSwitchStatus(switchStatusBytes);
		}
	}
	
	public byte[] getLedLineBytes() {
		return simModel.getLedLineBytes();
	}
	
	public int getSwtichChangeId(byte[] input) {
		return simModel.getSwtichChangeId(input);
	}
	
	public void updateChain(int pos) {
		simModel.updateChain(pos);
	}
	
	public WorkStatus getStatus() {
		return status;
	}

	public void setStatus(WorkStatus status) {
		this.status = status;
	}
	
	public enum WorkStatus {
		IDLE, RUNNING_STUDENT, RUNNING_TEACHER;
	}
}
