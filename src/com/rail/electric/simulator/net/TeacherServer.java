package com.rail.electric.simulator.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeacherServer {
	private final static Logger logger =  LoggerFactory.getLogger(TeacherServer.class);
	
	private int port = 9876;
	private boolean isRunning = true;

	private Socket clientSocket;
	
	public TeacherServer(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			
			clientSocket = serverSocket.accept();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
