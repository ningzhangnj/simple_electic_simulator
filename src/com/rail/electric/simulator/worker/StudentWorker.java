package com.rail.electric.simulator.worker;

import static com.rail.electric.simulator.SimulatorManager.QUIZ_CORRECT_HEAD_BYTE;
import static com.rail.electric.simulator.SimulatorManager.QUIZ_INITSTATE_HEAD_BYTE;
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
	private BlockingQueue<Boolean> resultQueue = new LinkedBlockingQueue<Boolean>();

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
					logger.info("Start quiz: {}", new String(ByteBuffer.wrap(receivedBytes, 1, receivedBytes.length-1).array()));
					break;
				case QUIZ_INITSTATE_HEAD_BYTE:
					ByteBuffer bb = ByteBuffer.wrap(receivedBytes, 1, receivedBytes.length-1);
					manager.updateInitState(bb.array());
					break;	
				case QUIZ_CORRECT_HEAD_BYTE:
					resultQueue.offer(true);
					break;
				case QUIZ_WRONG_HEAD_BYTE:
					resultQueue.offer(false);
					break;
				default:
					break;
			}	
		}
	}
	
	public boolean sendMessageAndWaitBooleanResult(byte[] message) {
		if (serverSocket != null) {
			try {
				serverSocket.getOutputStream().write(message);
				serverSocket.getOutputStream().flush();
			} catch (IOException e) {
				logger.error("Failed to write socket on student workstation. Caused by {}", e.toString());
			}
			try {
				return this.resultQueue.take();
			} catch (InterruptedException e) {
				logger.error("Failed to take result from queue. Caused by {}", e.toString());
			}
		}
		return false;
	}
	
	public void stop() {
		setRunning(false);
		if (clientFuture != null) {
			try {
				clientFuture.wait();
			} catch (InterruptedException e1) {
				logger.error("Failed to wait future to end. Caused by {}", e1.toString());
			}
		}
		if (serverSocket != null && serverSocket.isConnected()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close socket on student workstation. Caused by {}", e.toString());
			}
		}
	}
}
