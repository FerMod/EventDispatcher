package com.fermod.EventObserver.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.jupiter.api.Assertions.*;

class ValueChangeListenerTest {
	
	boolean eventInvoked = false;
	boolean eventMethodInvoked = false;

	@BeforeEach
	void beforeEach() {
		eventInvoked = false;
		eventMethodInvoked = false;
	}
	
	@DisplayName("Test Event - Method Invocation")
	@ParameterizedTest
	@CsvSource({"40", "50"})
	void testMethodInvocation(int oldValue, int newValue) {
		try {

//			startDate = TestUtilities.parseToDate(date1);
//			endDate = TestUtilities.parseToDate(date2);
//			offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
//
//			rh.registerListener((optValue) -> {
//				eventInvoked = true;
//				assertEquals(optValue.get(), offer, () -> "Value missmatch in event invocation.");
//			});
//
//			rh.addOffer(offer);
//
//			assertTrue(eventInvoked, () -> "Event method not invoked.");

		} catch (Exception e) {
//			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}		
	}

	@DisplayName("Test Event - Method Invocation Correct Value")
	@ParameterizedTest
	@Parameters
	void testMethodInvocationCorrectValue() {
		try {
//
//			startDate = TestUtilities.parseToDate(date1);
//			endDate = TestUtilities.parseToDate(date2);
//			offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
//
//			rh.registerListener((optValue) -> {
//				eventInvoked = true;
//				assertEquals(optValue.get(), offer, () -> "Value missmatch in event invocation.");
//			});
//
//			rh.addOffer(offer);
//
//			assertTrue(eventInvoked, () -> "Event method not invoked.");

		} catch (Exception e) {
//			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}		
	}
	
	private void eventMethod(int oldValue, int newValue) {
		eventMethodInvoked = true;
		assertEquals(oldValue, newValue, () -> "Value missmatch in the event method invocation.");
	}

}
