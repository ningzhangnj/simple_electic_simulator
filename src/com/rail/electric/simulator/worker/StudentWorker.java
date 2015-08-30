package com.rail.electric.simulator.worker;

import static com.rail.electric.simulator.SimulatorManager.QUIZ_CORRECT_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_INITSTATE_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_PASS_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_SUBJECT_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_WRONG_HEAD_BYTE;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.rail.electric.simulator.SimulatorManager;
import com.rail.electric.simulator.helpers.DataTypeConverter;

public class StudentWorker {
	private final static Logger logger =  LoggerFactory.getLogger(StudentWorker.class);	
	
	private int port = 9876;
	private String ip = "127.0.0.1";
	private SimulatorManager manager;
	private boolean isRunning = false;
	@SuppressWarnings("rawtypes")
	private Future clientFuture; 
	private BlockingQueue<Integer> resultQueue = new LinkedBlockingQueue<Integer>();

	private Socket serverSocket;
	
	public StudentWorker(String ip, int port, SimulatorManager manager) {
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
		clientFuture = Executors.newCachedThreadPool().submit(new Runnable() {

			@Override
			public void run() {
				try {
					serverSocket = new Socket(ip, port);
					clientMessageHandlingLoop(serverSocket.getInputStream());
				} catch (IOException e) {
					logger.error("Failed to create socket on student workstation. Caused by {}", e.toString());
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
					logger.info("Start quiz: {}", new String(bb.array()));
					break;
				case QUIZ_INITSTATE_HEAD_BYTE:
					final ByteBuffer bb1 = ByteBuffer.allocate(receivedBytes.length-1);
					bb1.put(receivedBytes, 1, receivedBytes.length-1);
					logger.info("Received switch init state bytes: {}", DataTypeConverter.bytesToHex(receivedBytes));
					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							manager.updateInitState(bb1.array());
						}
						
					});
					
					break;	
				case QUIZ_CORRECT_HEAD_BYTE:
					resultQueue.offer(0);
					break;
				case QUIZ_WRONG_HEAD_BYTE:
					resultQueue.offer(-1);
					break;
				case QUIZ_PASS_HEAD_BYTE:
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
				logger.debug("sendMessageAndWaitBooleanResult: {}", DataTypeConverter.bytesToHex(message));
				serverSocket.getOutputStream().write(message);
				serverSocket.getOutputStream().flush();
			} catch (IOException e) {
				logger.error("Failed to write socket on student workstation. Caused by {}", e.toString());
			}
			try {
				int result = this.resultQueue.take();
				if (result < 0) {
					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openError(Display.getCurrent().getActiveShell(), "Wrong operation", "Wrong operation, please retry.");							
						}						
						
					});
				} else if (result >= 1) {
					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Quiz passed", "Congratulations! You have passed the quiz.");							
						}						
						
					});
					manager.deactivate();
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
