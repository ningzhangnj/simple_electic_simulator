package com.rail.electric.simulator.net;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentClient {
	private final static Logger logger =  LoggerFactory.getLogger(StudentClient.class);
	
	private int port = 9876;
	private String ip = "127.0.0.1";
	private boolean isRunning = true;

	private Socket serverSocket;
	
	public StudentClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void start() {
		try {
			serverSocket = new Socket(ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
