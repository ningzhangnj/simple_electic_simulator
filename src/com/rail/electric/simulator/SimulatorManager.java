package com.rail.electric.simulator;

import static com.rail.electric.simulator.SimulatorFiguresCollections.SWITCH_OFFSET;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

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
	public static final byte QUIZ_PASS_HEAD_BYTE = (byte)0xf3;
	public static final byte SWITCH_STATUS_HEAD_BYTE = (byte)0xf9;
	
	private SimulatorFiguresCollections simModel;
	private SimulatorView simView;
	private WorkStatus status;
	private FreeformLayer primaryLayer;	
	
	private StudentWorker studentWorker;
	private TeacherWorker teacherWorker;
	
	private static  SimulatorManager instance;
	
	public static SimulatorManager getInstance(SimulatorView view, FreeformLayer layer,
			WorkStatus status) {
		if (instance == null) {
			instance = new SimulatorManager(view, layer, status);
		}
		return instance;
	}
	
	public static SimulatorManager getInstance() {
		return instance;
	}
	
	private SimulatorManager(SimulatorView view, FreeformLayer layer,
			WorkStatus status) {
		this.simView = view;
		this.primaryLayer = layer;
		this.status = status;
		simModel = new SimulatorFiguresCollections(primaryLayer, this);
	}

	public void deactivate() {		
		stop();
		setStatus(WorkStatus.IDLE);
		simModel.deactivate();		
		simView.updateMenuItems();		
	}

	public void importConnections(File file) {
		simModel.importConnections(file);
	}
	
	public void startTeacher(int port, WorkMode mode, String commPort) {
		simModel.init();
		simModel.activate();
		
		teacherWorker = new TeacherWorker(port, mode, commPort, this);

		teacherWorker.start();
	}
	
	public void stopTeacher() {
		if (teacherWorker != null) {
			teacherWorker.stop();
		}
	}
	
	public void startStudent(String ip, int port) {
		simModel.init();
		simModel.activate();
		studentWorker = new StudentWorker(ip, port, this);
		
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
		
	public void updateInitState(byte[] initStateBytes) {
		simModel.updatInitState(initStateBytes);
	}
	
	public void writeInitStateBytes(DataOutputStream output) {
		simModel.writeInitStateBytes(output, QUIZ_INITSTATE_HEAD_BYTE);
	}
	
	public void writeQuizNameBytes(DataOutputStream output) {
		simModel.writeQuizNameBytes(output, QUIZ_SUBJECT_HEAD_BYTE);
	}
	
	public int validate(int id, boolean state) {
		if (status == WorkStatus.RUNNING_STUDENT) {
			ByteBuffer bb = ByteBuffer.allocate(7);
			bb.putInt(3);
			bb.put(4, SWITCH_STATUS_HEAD_BYTE);
			bb.put(5, (byte)(id - SWITCH_OFFSET));
			bb.put(6, state?(byte)1:(byte)0);
			return studentWorker.sendMessageAndWaitBooleanResult(bb.array());
		}
		else if (status == WorkStatus.RUNNING_TEACHER) return 1;
		return 1;
	}
	
	public int validateAndUpdateSwitchStatus(byte[] switchStatusBytes) {
		int result = simModel.validate(switchStatusBytes);
		if (result >= 0) {
			simModel.updateSwitchStatus(switchStatusBytes);
		}
		return result;
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
	
	public String checkSwitchStatus(byte[] input) {
		return simModel.checkSwitchStatus(input);
	}
	
	public List<String> getOperationList() {
		return simModel.getOperationList();
	}
	
	public void sendLineStatus() {
		if (teacherWorker != null && teacherWorker.getMode() == WorkMode.STUDENT_TEACHER_SIMULATOR) {
			teacherWorker.sendLineStatus();
		}
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
