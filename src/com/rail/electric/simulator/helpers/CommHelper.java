package com.rail.electric.simulator.helpers;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CommHelper {
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

	public void writeBytes(CommPortIdentifier portId, byte[] bytes) {
		try {
        	SerialPort serialPort = (SerialPort)portId.open("Teacher", 2000);//timeout- 2000ms
			serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			OutputStream out=serialPort.getOutputStream();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
