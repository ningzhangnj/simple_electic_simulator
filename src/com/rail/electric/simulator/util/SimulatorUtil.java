package com.rail.electric.simulator.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.helpers.DataTypeConverter;

public class SimulatorUtil {
	private final static Logger logger =  LoggerFactory.getLogger(SimulatorUtil.class);	
	
	private static final List<Long> licenses = Arrays.asList(132837597590628L, 122178626107566L, 125099203868846L, 
												121488996188301L, 118933490647181L);
	
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			logger.error("Exceptional interrupt happened, caused by {}", e.toString());
		}
	}
	
	public static void sleepMilliSeconds(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			logger.error("Exceptional interrupt happened, caused by {}", e.toString());
		}
	}
	
	public static  boolean calculateLicense() {	
		try {			
			for(Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
               e.hasMoreElements(); )
			{
				NetworkInterface ni = e.nextElement();
				byte[] mac = ni.getHardwareAddress();
				if (mac == null || mac.length != 6) continue;
				logger.debug("Get mac address is : " + DataTypeConverter.bytesToHex(mac));				
				long result = (convertByte2Long(mac[0])<<8) + convertByte2Long(mac[1])
						+ (convertByte2Long(mac[2])<<24) + (convertByte2Long(mac[3])<<16) 
						+ (convertByte2Long(mac[4])<<40) + (convertByte2Long(mac[5])<<32);
				logger.debug("Computed license result is: " + result);
				if (licenses.contains(result)) return true;
			}			
		} catch (SocketException e) {
			logger.error("Critial error: can not get network info.");
		}
		
		return false;
	}
	
	private static long convertByte2Long(byte b) {
		return b&0xff;
	}
}
