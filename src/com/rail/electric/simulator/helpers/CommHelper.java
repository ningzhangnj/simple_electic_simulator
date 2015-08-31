package com.rail.electric.simulator.helpers;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommHelper {
	private final static Logger logger =  LoggerFactory.getLogger(CommHelper.class);	
	
	private SerialPort serialPort;
	
	private boolean isCommPortConnected = false;
	
	public List<CommPortIdentifier> getComPorts() {
		List<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
		@SuppressWarnings("rawtypes")
		Enumeration portList = CommPortIdentifier.getPortIdentifiers(); 
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
		    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
		    	ports.add(portId);
		    }
		}
		return ports;
	}
	
	public boolean open(String  portName) {		
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(portName)) {
		    	try {
		    		serialPort = (SerialPort)portId.open("Teacher", 5000);//timeout- 5000ms
		    		serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		    		isCommPortConnected = true;
		    		return true;
				} catch (UnsupportedCommOperationException e) {
					logger.error("Unsupported comm operation. Caused by {}", e.toString());
				} catch (PortInUseException e1) {
					logger.error("Port {} is already in use. Caused by {}", portName, e1.toString());
				} 
		    }
		} catch (NoSuchPortException e2) {
			logger.error("Port {} is not found. Caused by {}", portName, e2.toString());;
		}
	    
		return false;
	}
	
	public void close() {
		if (serialPort != null) {
			isCommPortConnected = false;
			serialPort.close();
		}		
	}

	public void writeBytes(byte[] bytes) {
		try {        	
			OutputStream out = serialPort.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			logger.error("Failed to write out bytes {}, caused by {}", DataTypeConverter.bytesToHex(bytes), e.toString());
		}
	}
	
	public byte[] readBytes(int length) {
		int byteLeft = length;
        int offset = 0;
        byte[] bytes = new byte[length];        
        try {
        	InputStream din = serialPort.getInputStream();
            while (byteLeft > 0) {
                int len = din.read(bytes, offset, byteLeft);
                offset += len;
                byteLeft -= len;
            }
            return bytes;
        } catch (IOException e) {
        	logger.error("Failed to operate on serial port, caused by {}", e.toString());
        }
        return  null;
		
	}
	
	public boolean isDataAvailable() {
		try {
			return serialPort.getInputStream().available() > 0;
		} catch (IOException e) {
			logger.error("Failed to operate on serial port, caused by {}", e.toString());
		}
		return false;
	}

	public static Logger getLogger() {
		return logger;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public boolean isCommPortConnected() {
		return isCommPortConnected;
	}
	
	
}
