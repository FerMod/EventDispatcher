package com.fermod;

import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fermod.extension.TimingExtension;
import com.fermod.observer.ObservedValue;
import com.fermod.testdata.serializable.PersonObject;
import com.fermod.annotations.ArraySource;
import com.fermod.annotations.ArraySources;
import com.fermod.event.ValueChangeListener;

@ExtendWith({TimingExtension.class})
class ObservableValueTest {

	private static final Logger LOGGER = LogManager.getLogger(ObservableValueTest.class);

	static File tempFile;
	static boolean eventMethodInvoked;
	static boolean listenerMethodInvoked;

	@BeforeEach
	void beforeEach() {
		initTempFile("SerializedObjectTest");
		eventMethodInvoked = false;
		listenerMethodInvoked = false;
	}

	private void initTempFile(String fileName) {
		try {
			tempFile = File.createTempFile(fileName, ".tmp");
			tempFile.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(tempFile != null) {
				tempFile.delete();
			}
		}
	}

	@DisplayName("Test Event - Unregister event")
	@ParameterizedTest
	@ValueSource(ints = { 10, 0, -4 })
	void testUnregisterEvent(int value, TestInfo testInfo) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(value);

		ValueChangeListener<Integer> valueChangeListener = new ValueChangeListener<Integer>() {
			@Override
			public void onValueChanged(Integer oldValue, Integer value) {
				listenerMethodInvoked = true;
			}
		};
		observedValue.registerListener(valueChangeListener);

		try {
			assumeFalse(listenerMethodInvoked, () -> "Listener method already invoked");
			observedValue.unregisterListener(valueChangeListener);
			observedValue.set(value);
			assertFalse(listenerMethodInvoked, () -> "Listener method invoked");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}

	}

	@DisplayName("Test Event - Unregister all events")
	@ParameterizedTest
	@ValueSource(ints = { 10, 0, -4 })
	void testUnregisterAllEvents(int value, TestInfo testInfo) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(value);

		ValueChangeListener<Integer> valueChangeListener = new ValueChangeListener<Integer>() {
			@Override
			public void onValueChanged(Integer oldValue, Integer value) {
				listenerMethodInvoked = true;
			}
		};
		observedValue.registerListener(valueChangeListener);

		observedValue.registerListener(ObservableValueTest::valueChangedTest);

		try {
			assumeFalse(eventMethodInvoked, () -> "Event method already invoked");
			assumeFalse(listenerMethodInvoked, () -> "Listener method already invoked");
			observedValue.unregisterAllListeners();
			observedValue.set(value);
			assertFalse(eventMethodInvoked, () -> "Event method invoked");
			assertFalse(listenerMethodInvoked, () -> "Listener method invoked");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}

	}

	@DisplayName("Test Event - Inline method invocation")
	@ParameterizedTest
	@CsvSource({"30, 10", "-2, 10", "11, 10"})
	void testEventMethodInvocation(int expected, int value, TestInfo testInfo) {

		try {
			ObservedValue<Integer> observedValue = new ObservedValue<>(value);
			observedValue.registerListener((oldVAlue, newValue) -> {
				eventMethodInvoked = true;
				assertAll("EventValues",
					() -> assertEquals(oldVAlue, value, () -> "New value missmatch in event invocation."),
					() -> assertEquals(newValue, expected, () -> "Old value missmatch in event invocation.")
				);
			});

			observedValue.set(expected);

			assertTrue(eventMethodInvoked, () -> "Event method not invoked.");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}
	}

	@DisplayName("Test Event - Referenced method invocation")
	@ParameterizedTest
	@CsvSource({"Paco, NewPaco, 44", "Lola, NewLola, 41"})
	void testEventMethodInvocation(String name, String newName, int age) {

		PersonObject personTest = new PersonObject(name, age);

		try {
			personTest.onNameChanged(ObservableValueTest::valueChangedTest);
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertFalse(eventMethodInvoked, () -> "Event method already invoked");
		personTest.setName(newName);
		assertTrue(eventMethodInvoked, () -> "Event method not invoked");

	}

	@DisplayName("Test Event - Referenced method invocation")
	@ParameterizedTest
	@CsvSource({"Paco, NewPaco, 44", "Lola, NewLola, 41"})
	void testEventMethodValueChange(String name, String newName, int age) {

		PersonObject personTest = new PersonObject(name, age);

		try {
			personTest.onNameChanged(ObservableValueTest::valueChangedTest);
		} catch (Exception e) {
			assumeNoException(e);
		}

		assumeFalse(eventMethodInvoked, () -> "Event method already invoked");
		personTest.setName(newName);
		assumeTrue(eventMethodInvoked, () -> "Event method not invoked");

		assertEquals(newName, personTest.getName(), () -> "Obtained value is the new value");

	}

	@DisplayName("Test Event - Event not fired")
	@ParameterizedTest
	@ValueSource(ints = { 10, 0, -4 })
	void testEventMethodNotInvoqued(int value, TestInfo testInfo) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(value);
		observedValue.registerListener(ObservableValueTest::valueChangedTest);

		try {
			assertFalse(eventMethodInvoked, () -> "Event method already invoked");
			observedValue.set(value);
			assertFalse(eventMethodInvoked, () -> "Event method invoked");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Boolean")
	@ParameterizedTest
	@CsvSource({"true", "false"})	
	void testSerialization(boolean expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Boolean> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Boolean> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Boolean>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Byte")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(byte expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Byte> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Byte> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Byte>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Char")
	@ParameterizedTest
	@CsvSource({"A", "T"})		
	void testSerialization(char expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Character> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Character> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Character>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Short")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(short expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Short> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Short> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Short>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Int")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(int expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Integer> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Integer> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Integer>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Long")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(long expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Long> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Long> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Long>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Float")
	@ParameterizedTest
	@CsvSource({"65f"})		
	void testSerialization(float expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Float> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Float> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Float>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Double")
	@ParameterizedTest
	@CsvSource({"65.55"})	
	void testSerialization(double expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<Double> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<Double> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<Double>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - String")
	@ParameterizedTest
	@CsvSource({"'Test of string', 'Another test'"})		
	void testSerialization(String expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<String> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<String> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<String>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expected, value.get());

	}

	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Object")
	@ParameterizedTest
	@CsvSource({"Paco, 44", "Lola, 41"})
	void testSerialization(String name, int age) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		PersonObject expectedTestClass = new PersonObject(name, age);
		ObservedValue<PersonObject> observedValue = new ObservedValue<PersonObject>(expectedTestClass);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<PersonObject> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<PersonObject>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertEquals(expectedTestClass, value.get(), "" + observedValue.get().hashCode() + " " + value.hashCode());				
	}


	@SuppressWarnings("unchecked")
	@DisplayName("Test Serialization - Array of int")
	@ParameterizedTest
	@ArraySources(arrays = {
		@ArraySource(array = {1, 2, 3, 4, 5, 6}),
		@ArraySource(array = {21, 34, 68}),
		@ArraySource(array = {72, 84, 78})
	})
	void testSerialization(int[] expected) {

		assumeTrue(tempFile != null);
		File file = tempFile;

		ObservedValue<int[]> observedValue = new ObservedValue<>(expected);

		serialiceToFile(file, observedValue);
		assumeTrue(file.length() > 0);

		ObservedValue<int[]> value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = (ObservedValue<int []>) objectInputStream.readObject();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}

		assertTrue(Arrays.equals(expected, value.get()));
	}

	private static <T> void valueChangedTest(T oldValue, T newValue) {
		eventMethodInvoked = true;
		LOGGER.debug("Value changed event metod called. [oldValue: " + oldValue + ", newValue: " + newValue + "]");
	}

	private <T> void serialiceToFile(File file, T value) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
				objectOutputStream.writeObject(value);	
				objectOutputStream.flush();
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
	}

}
