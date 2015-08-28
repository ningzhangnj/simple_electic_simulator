package com.rail.electric.simulator.helpers;

import java.io.DataInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.worker.StudentWorker;

public class DataTypeConverter {
	private final static Logger logger =  LoggerFactory.getLogger(DataTypeConverter.class);
	
	protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static byte[] readBytes(DataInputStream input) {
		try {
			int length = input.readInt();
			byte[] bb = new byte[length];
			input.read(bb);
			return bb;
		} catch (IOException e) {
			logger.error("Failed to read bytes. caused by {}", e.toString());
		}
		return null;
	}
}
