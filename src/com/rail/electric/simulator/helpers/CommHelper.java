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

public class CommHelper {
	private SerialPort serialPort;
	
	public List<CommPortIdentifier> getComPorts() {
		List<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers(); 
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
		    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
		    	ports.add(portId);
		    }
		}
		return ports;
	}
	
	public void open(String  portName) {		
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(portName)) {
		    	try {
		    		serialPort = (SerialPort)portId.open("Teacher", 5000);//timeout- 5000ms
		    		serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);			
				} catch (UnsupportedCommOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PortInUseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
		    	return;
		    }
		} catch (NoSuchPortException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    
		
	}
	
	public void close() {
		serialPort.close();
	}

	public void writeBytes(byte[] bytes) {
		try {        	
			OutputStream out=serialPort.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
            //TODO
            e.printStackTrace();
        }
        return  null;
		
	}
}
