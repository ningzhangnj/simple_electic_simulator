package com.rail.electric.simulator.worker;

import static com.rail.electric.simulator.SimulatorFiguresCollections.SWITCH_NUMBERS;
import static com.rail.electric.simulator.SimulatorManager.BEGIN_BYTE;
import static com.rail.electric.simulator.SimulatorManager.CORRECT_PACKET_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_CORRECT_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_PASS_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_WRONG_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.READ_SWITCH_BYTE;
import static com.rail.electric.simulator.SimulatorManager.SWITCH_STATUS_HEAD_BYTE;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.SimulatorManager;
import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.dialogs.OperationInfoDialog;
import com.rail.electric.simulator.helpers.CommHelper;
import com.rail.electric.simulator.helpers.DataTypeConverter;
import com.rail.electric.simulator.model.TeacherWorkstation.WorkMode;
import com.rail.electric.simulator.util.SimulatorUtil;

public class TeacherWorker {
private final static Logger logger =  LoggerFactory.getLogger(TeacherWorker.class);
	
	private int port = 9876;
	private WorkMode mode;
	private String commPort;
	private SimulatorManager manager;
	private boolean isRunning = false;
	private CommHelper commHelper = new CommHelper();
	
	private boolean hasInitSwitchStatus =  false;

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
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	private synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}	

	public void start() {
		setRunning(true);
		if (mode != WorkMode.STUDENT_TEACHER) {
			if (commHelper.open(commPort)) {
				commFuture = Executors.newCachedThreadPool().submit(new Runnable() {
	
					@Override
					public void run() {
						if (mode == WorkMode.TEACHER_SIMULATOR) {
							sendSwitchInitStatusRequest();
						}
						commMessageHandlingLoop();
					}
					
				});
			} else {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), SimulatorMessages.ErrorDialog_title, 
						SimulatorMessages.OpenCommError_message + commPort);
			}
		}
		if (mode != WorkMode.TEACHER_SIMULATOR) {
			serverFuture = Executors.newCachedThreadPool().submit(new Runnable() {
	
				@Override
				public void run() {
					try {
						serverSocket = new ServerSocket();
						serverSocket.setReuseAddress(true);
						serverSocket.bind(new InetSocketAddress(port));
						
						clientSocket = serverSocket.accept();
						DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
						DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
						manager.writeQuizNameBytes(dataOut);
						manager.writeInitStateBytes(dataOut);
						serverMessageHandlingLoop(dataIn, dataOut);
						
					} catch (IOException e) {
						logger.error("Failed to create socket on teacher workstation. Caused by {}", e.toString());
					}				
				}
				
			});
		}
		
	}	
	
	private void serverMessageHandlingLoop(DataInputStream dataIn, final DataOutputStream dataOut) {
		while (isRunning()) {
			byte[] receivedBytes = DataTypeConverter.readBytes(dataIn);
			switch(receivedBytes[0]) {
				case SWITCH_STATUS_HEAD_BYTE:
					final ByteBuffer bb = ByteBuffer.allocate(receivedBytes.length-1);
					bb.put(receivedBytes, 1, receivedBytes.length-1);
					logger.debug("Received switch status: {}", DataTypeConverter.bytesToHex(bb.array()));
					
					
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							int result = manager.validateAndUpdateSwitchStatus(bb.array());
							respondSwitchValidation(result, dataOut);
							sendLineStatus();
							if (result > 0) {
								OperationInfoDialog dialog = new OperationInfoDialog(Display.getCurrent().getActiveShell(), 
										SimulatorMessages.OperationFinished_Title, 
										SimulatorMessages.QuizPassedTeacher_message,
										manager.getOperationList());
								dialog.open();
								manager.deactivate();
							}
						}
						
					});
					
					break;				
				default:
					break;
			}
	    }
	}
	
	private void respondSwitchValidation(int flag, DataOutputStream dataOut) {
		ByteBuffer bb = ByteBuffer.allocate(5);
		bb.putInt(1);
		if (flag == 0 ) {			
			bb.put(4, QUIZ_CORRECT_HEAD_BYTE);			
		} else if (flag == -1){			
			bb.put(4, QUIZ_WRONG_HEAD_BYTE);
		} else {
			bb.put(4, QUIZ_PASS_HEAD_BYTE);
		}
		
		try {
			dataOut.write(bb.array());
			dataOut.flush();
			logger.debug("Respond switch validation result {}", DataTypeConverter.bytesToHex(bb.array()));
		} catch (IOException e) {
			logger.error("Failed to respond switch validation result {}, caused by {}", DataTypeConverter.bytesToHex(bb.array()), 
					e.toString());
		}
	}
	
	private void commMessageHandlingLoop() {
		while (isRunning()) {
			if (mode == WorkMode.STUDENT_TEACHER_SIMULATOR) {
				while (commHelper.isDataAvailable()) {
					readSwitchInitStatus();
					SimulatorUtil.sleepMilliSeconds(100);
				}
				
			} else {				
				if (hasInitSwitchStatus) {
					if(commHelper.isDataAvailable()) {
						readSwitchStatus();
					}
				} else {
					final String status = readSwitchInitStatus();
					if (status == null) {
						sendLineStatus();
						hasInitSwitchStatus = true;
					} else {
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								MessageDialog.openError(Display.getCurrent().getActiveShell(), 
										SimulatorMessages.ErrorDialog_title, 
										SimulatorMessages.ErrorSwitchStatus_message + status);
								while (commHelper.isDataAvailable()) {
									readSwitchInitStatus();
									SimulatorUtil.sleepMilliSeconds(100);
								}
								sendSwitchInitStatusRequest();
							}
							
						});							
					}
				}
			}
		}
	}
	
	public void sendLineStatus() {
		if (!commHelper.isCommPortConnected()) return;
		byte[] result = manager.getLedLineBytes();
		logger.debug("Line status: " + DataTypeConverter.bytesToHex(result));
		for (int i=0; i<3; i++) {
			commHelper.writeBytes(result);
			byte[] response = commHelper.readBytes(1);
			logger.debug("Response of line status: " + DataTypeConverter.bytesToHex(response));
			if (response[0] == CORRECT_PACKET_BYTE) break;
		}		
	}
	
	private void sendSwitchInitStatusRequest() {
		byte[] request = new byte[] {READ_SWITCH_BYTE};
		commHelper.writeBytes(request);
		logger.debug("Request switch init status : {}", DataTypeConverter.bytesToHex(request));
	}
	
	private String readSwitchInitStatus() {
		byte[] result = commHelper.readBytes(1);
		if (result[0] != BEGIN_BYTE ) {
			commHelper.readBytes(1);
		}
		byte[] scanSwitchStatus = commHelper.readBytes(SWITCH_NUMBERS);
		logger.debug("Read swtich init status is: {}", DataTypeConverter.bytesToHex(scanSwitchStatus));
		String flag = manager.checkSwitchStatus(scanSwitchStatus);
		
		byte[] endByte = commHelper.readBytes(1);
		logger.debug("End byte of switch status is: {}", DataTypeConverter.bytesToHex(endByte));
		return flag;
	}
	
	private void readSwitchStatus() {
		
		byte[] result = commHelper.readBytes(1);
		if (result[0] != BEGIN_BYTE ) {
			commHelper.readBytes(1);
		}
		byte[] scanSwitchStatus = commHelper.readBytes(SWITCH_NUMBERS);
		logger.debug("Scanned swtich status is: {}", DataTypeConverter.bytesToHex(scanSwitchStatus));
		final int pos = manager.getSwtichChangeId(scanSwitchStatus);
		if (pos < 0) return;
		
		byte[] endByte = commHelper.readBytes(1);
		logger.debug("End byte of switch status is: {}", DataTypeConverter.bytesToHex(endByte));
		commHelper.writeBytes(new byte[]{CORRECT_PACKET_BYTE});
		
		if (mode == WorkMode.TEACHER_SIMULATOR) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					manager.updateChain(pos);
					sendLineStatus();
				}
				
			});			
		}				
	}
	
	public void stop() {
		setRunning(false);
		
		SimulatorUtil.sleepSeconds(1);
		
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
		if (commFuture != null) {
			commFuture.cancel(true);
		}
		
		if (serverFuture != null) {
			serverFuture.cancel(true);
		}
		
		if (commHelper != null ) commHelper.close();
	}

	public WorkMode getMode() {
		return mode;
	}
	
	
}
