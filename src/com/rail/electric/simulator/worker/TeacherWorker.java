package com.rail.electric.simulator.worker;

import static com.rail.electric.simulator.SimulatorManager.BEGIN_BYTE;
import static com.rail.electric.simulator.SimulatorManager.CORRECT_PACKET_BYTE;
import static com.rail.electric.simulator.SimulatorManager.SWITCH_STATUS_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorFiguresCollections.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.SimulatorManager;
import com.rail.electric.simulator.figures.StateFigure;
import com.rail.electric.simulator.helpers.CommHelper;
import com.rail.electric.simulator.helpers.DataTypeConverter;
import com.rail.electric.simulator.model.TeacherWorkstation.WorkMode;

public class TeacherWorker {
private final static Logger logger =  LoggerFactory.getLogger(TeacherWorker.class);
	
	private int port = 9876;
	private WorkMode mode;
	private String commPort;
	private SimulatorManager manager;
	private boolean isRunning = false;
	private CommHelper commHelper = new CommHelper();

	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@SuppressWarnings("rawtypes")
	private Future serverFuture;
	@SuppressWarnings("rawtypes")
	private Future commFuture;
	
	public TeacherWorker(int port, WorkMode mode, String commPort, SimulatorManager manager) {
		this.port = port;
		this.mode = mode;
		this.commPort = commPort;
		this.manager = manager;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public void start() {
		setRunning(true);
		if (mode != WorkMode.STUDENT_TEACHER) {
			commHelper.open(commPort);
			commFuture = Executors.newCachedThreadPool().submit(new Runnable() {

				@Override
				public void run() {
					commMessageHandlingLoop();
				}
				
			});
		}
		serverFuture = Executors.newCachedThreadPool().submit(new Runnable() {

			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(port);
					
					clientSocket = serverSocket.accept();
					DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
					manager.writeQuizNameBytes(dataOut);
					manager.writeInitStateBytes(dataOut);
					serverMessageHandlingLoop(clientSocket.getInputStream());
					
				} catch (IOException e) {
					logger.error("Failed to create socket on teacher workstation. Caused by {}", e.toString());
				}				
			}
			
		});
		
	}	
	
	private void serverMessageHandlingLoop(InputStream input) {
		DataInputStream dataIn = new DataInputStream(input);
		while (isRunning()) {
			byte[] receivedBytes = DataTypeConverter.readBytes(dataIn);
			switch(receivedBytes[0]) {
				case SWITCH_STATUS_HEAD_BYTE:
					ByteBuffer bb = ByteBuffer.wrap(receivedBytes, 1, receivedBytes.length-1);
					manager.validateAndUpdateSwitchStatus(bb.array());
					break;				
				default:
					break;
			}
	    }
	}
	
	private void commMessageHandlingLoop() {
		while (isRunning()) {
			readSwitchStatus();
		}
	}
	
	public void sendLineStatus() {
		byte[] result = manager.getLedLineBytes();
		logger.debug("Line status: " + DataTypeConverter.bytesToHex(result));
		for (int i=0; i<3; i++) {
			commHelper.writeBytes(result);
			byte[] response = commHelper.readBytes(1);
			logger.debug("Response of line status: " + DataTypeConverter.bytesToHex(response));
			if (response[0] == CORRECT_PACKET_BYTE) break;
		}		
	}
	
	private void readSwitchStatus() {
		
		byte[] result = commHelper.readBytes(1);
		if (result[0] != BEGIN_BYTE ) {
			commHelper.readBytes(1);
		}
		byte[] scanSwitchStatus = commHelper.readBytes(SWITCH_NUMBERS);
		logger.debug("Scanned swtich status is: {}", DataTypeConverter.bytesToHex(scanSwitchStatus));
		int pos = manager.getSwtichChangeId(scanSwitchStatus);
		if (pos < 0) return;
		
		byte[] endByte = commHelper.readBytes(1);
		logger.debug("End byte of switch status is: {}", DataTypeConverter.bytesToHex(endByte));
		commHelper.writeBytes(new byte[]{CORRECT_PACKET_BYTE});
		
		if (mode == WorkMode.TEACHER_SIMULATOR) {
			manager.updateChain(pos);
		}
		
		sendLineStatus();		
	}
	
	public void stop() {
		if (commFuture != null) {
			try {
				commFuture.wait();
			} catch (InterruptedException e1) {
				logger.error("Failed to wait comm future to end. Caused by {}", e1.toString());
			}
		}
		
		if (serverFuture != null) {
			try {
				serverFuture.wait();
			} catch (InterruptedException e1) {
				logger.error("Failed to wait server future to end. Caused by {}", e1.toString());
			}
		}
		if (clientSocket != null && clientSocket.isConnected()) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close socket on teacher workstation. Caused by {}", e.toString());
			}
		}
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close server socket on teacher workstation. Caused by {}", e.toString());
			}
		}
		if (commHelper != null) commHelper.close();
	}
}
