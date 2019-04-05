package com.fermod;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jExample {

	private static final Logger LOGGER = LogManager.getLogger(Log4jExample.class);
	private static int count = 5;
	
	public static void main(String[] args) {

		while(count > 0) {
			LOGGER.trace("Trace log message");
			LOGGER.debug("Debug log message");
			LOGGER.info("Info log message");
			LOGGER.warn("Warn log message");
			LOGGER.error("Error log message");
			LOGGER.fatal("Fatal log message");
			System.out.println();
			count--;
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

	}

}
