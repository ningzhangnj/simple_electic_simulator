package com.rail.electric.simulator.worker;

import static com.rail.electric.simulator.manager.ConnectionsManager.QUIZ_CORRECT_HEAD_BYTE;
import static com.rail.electric.simulator.manager.ConnectionsManager.QUIZ_INITSTATE_HEAD_BYTE;
import static com.rail.electric.simulator.manager.ConnectionsManager.QUIZ_PASS_HEAD_BYTE;
import static com.rail.electric.simulator.manager.ConnectionsManager.QUIZ_SUBJECT_HEAD_BYTE;
import static com.rail.electric.simulator.manager.ConnectionsManager.QUIZ_WRONG_HEAD_BYTE;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.helpers.DataTypeConverter;
import com.rail.electric.simulator.manager.ConnectionsManager;
import com.rail.electric.simulator.util.SimulatorUtil;

public class StudentWorker {
	private final static Logger logger =  LoggerFactory.getLogger(StudentWorker.class);	
	
	private int port = 9876;
	private String ip = "127.0.0.1";
	private ConnectionsManager manager;
	private boolean isRunning = false;
	@SuppressWarnings("rawtypes")
	private Future clientFuture; 
	private BlockingQueue<Integer> resultQueue = new LinkedBlockingQueue<Integer>();

	private Socket serverSocket;
	
	public StudentWorker(String ip, int port, ConnectionsManager manager) {
		this.ip = ip;
		this.port = port;
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
		clientFuture = Executors.newSingleThreadExecutor().submit(new Runnable() {

			@Override
			public void run() {
				try {
					serverSocket = new Socket(ip, port);
					clientMessageHandlingLoop(serverSocket.getInputStream());
				} catch (IOException e) {
					logger.error("Failed to create socket on student workstation. Caused by {}", e.toString());
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openError(Display.getCurrent().getActiveShell(), 
									SimulatorMessages.ErrorDialog_title, SimulatorMessages.ConectionRefused_message);
							manager.deactivate();
						}
						
					});
					
				}				
			}
			
		});
		
	}
	
	private void clientMessageHandlingLoop(InputStream input) {
		DataInputStream dataIn = new DataInputStream(input);
		while (isRunning()) {
			byte[] receivedBytes = DataTypeConverter.readBytes(dataIn);
			switch(receivedBytes[0]) {
				case QUIZ_SUBJECT_HEAD_BYTE:
					ByteBuffer bb = ByteBuffer.allocate(receivedBytes.length-1);
					bb.put(receivedBytes, 1, receivedBytes.length-1);
					try {
						final String quizName = new String(bb.array(), "UTF-8");
						logger.info("Start quiz: {}", quizName);
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								MessageDialog.openInformation(Display.getCurrent().getActiveShell(), 
										SimulatorMessages.InfoDialog_title, SimulatorMessages.ReadyToStartQuiz_message + "\n" + quizName);	
							}
							
						});
					} catch (UnsupportedEncodingException e) {
						logger.error("UTF-8 charset is not supported. Caused by {}", e.toString());
					}
					break;
				case QUIZ_INITSTATE_HEAD_BYTE:
					final ByteBuffer bb1 = ByteBuffer.allocate(receivedBytes.length-1);
					bb1.put(receivedBytes, 1, receivedBytes.length-1);
					logger.info("Received switch init state bytes: {}", DataTypeConverter.bytesToHex(receivedBytes));
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							manager.updateInitState(bb1.array());
						}
						
					});
					
					break;	
				case QUIZ_CORRECT_HEAD_BYTE:
					logger.debug("Received quiz correct head.");
					resultQueue.offer(0);
					break;
				case QUIZ_WRONG_HEAD_BYTE:
					logger.debug("Received quiz wrong head.");
					resultQueue.offer(-1);
					break;
				case QUIZ_PASS_HEAD_BYTE:
					logger.debug("Received quiz pass head.");
					resultQueue.offer(1);
					break;
				default:
					break;
			}	
		}
	}
	
	public int sendMessageAndWaitBooleanResult(byte[] message) {
		if (serverSocket != null) {
			try {				
				serverSocket.getOutputStream().write(message);
				serverSocket.getOutputStream().flush();
				logger.debug("sendMessageAndWaitBooleanResult: {}", DataTypeConverter.bytesToHex(message));
			} catch (IOException e) {
				logger.error("Failed to write socket on student workstation. Caused by {}", e.toString());
			}
			try {
				SimulatorUtil.sleepMilliSeconds(50);
				int result = this.resultQueue.take();
				if (result < 0) {
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openError(Display.getCurrent().getActiveShell(), 
									SimulatorMessages.ErrorDialog_title, SimulatorMessages.WrongOperation_message);							
						}						
						
					});
				} else if (result >= 1) {
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), 
									SimulatorMessages.InfoDialog_title, SimulatorMessages.QuizPassedStudent_message);

							manager.deactivate();
						}						
						
					});
				}
				return result;
			} catch (InterruptedException e) {
				logger.error("Failed to take result from queue. Caused by {}", e.toString());
			}
		}
		return -1;
	}
	
	public void stop() {
		setRunning(false);
		if (serverSocket != null && serverSocket.isConnected()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close socket on student workstation. Caused by {}", e.toString());
			}
		}		
		
		if (clientFuture != null) {
			clientFuture.cancel(true);			
		}
		
	}
}
