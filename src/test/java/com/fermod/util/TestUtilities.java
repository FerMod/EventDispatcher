package com.fermod.util;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.TestInfo;

public final class TestUtilities {

	private TestUtilities() {}

	/**
	 * Parse <code>java.util.Date</code> to <code>java.time.LocalDate</code>.<p>
	 * Assumes that the parsed value will not be <code>null</code> and that the parse 
	 * will not throw any exceptions.<br>
	 * If the assumption fails, the test that calls the function will also fail.
	 * 
	 * @param localDate value to parse
	 * @return The date in <code>java.util.Date</code> type
	 */
	public static Date parseToDate(LocalDate localDate) {
		Date date = null;
		try {
			date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to parse date.", e);
		}
		assumeNotNull(date);
		return date;
	}
	
	/**
	 * Returns a formated error message with the exception information.
	 * 
	 * @param e the exception
	 * @param testInfo the <code>TestInfo</code> used, to inject information about the current test
	 * @return the formatted fail message
	 */
	public static String getFailMessage(Exception e, TestInfo testInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Unexpected exception thrown");
		if(testInfo.getTestMethod().isPresent()) {
			sb.append(" in " + testInfo.getTestMethod().get().getName());
		}
		sb.append("\n\tCase: " + testInfo.getDisplayName());
		return sb.toString();
	}

}
