package com.rail.electric.simulator.util;

public class SimulatorUtil {
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
