package com.rail.electric.simulator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.helpers.CommHelper;

public class SimulatorUtil {
	private final static Logger logger =  LoggerFactory.getLogger(CommHelper.class);	
	
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
}
