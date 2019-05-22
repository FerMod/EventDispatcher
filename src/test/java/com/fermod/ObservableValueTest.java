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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fermod.annotations.ArraySource;
import com.fermod.annotations.ArraySources;
import com.fermod.event.ValueChangeListener;
import com.fermod.extension.TimingExtension;
import com.fermod.observer.ObservedValue;
import com.fermod.testdata.serializable.PersonObject;

@ExtendWith({TimingExtension.class})
class ObservableValueTest {

	private static final Logger LOGGER = LogManager.getLogger(ObservableValueTest.class);

	static String fileName;
	static boolean eventMethodInvoked;
	static boolean listenerMethodInvoked;

	@BeforeAll
	static void beforeAll() {
		fileName = "SerializedObjectTest.tmp";
	}
	
	@BeforeEach
	void beforeEach() {
		eventMethodInvoked = false;
		listenerMethodInvoked = false;
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
			
			assumeFalse(eventMethodInvoked, () -> "Event method already invoked.");
			assumeFalse(listenerMethodInvoked, () -> "Listener method already invoked.");
			
			observedValue.unregisterAllListeners();
			observedValue.set(value);
			
			assertAll("EventValues",
				() -> assertFalse(eventMethodInvoked, () -> "Event method invoked."),
				() -> assertFalse(listenerMethodInvoked, () -> "Listener method invoked.")
			);
			
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

		assumeFalse(eventMethodInvoked, () -> "Event method already invoked");
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
			assumeFalse(eventMethodInvoked, () -> "Event method already invoked");
			observedValue.set(value);
			assertFalse(eventMethodInvoked, () -> "Event method invoked");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}

	}
	
	@DisplayName("Test Event - Enable/Disable value change notification")
	@ParameterizedTest
	@CsvSource({"true", "false"})
	void testEventNotification(boolean notifyChange, TestInfo testInfo) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(0);
		observedValue.registerListener(ObservableValueTest::valueChangedTest);

		try {
			assumeFalse(eventMethodInvoked, () -> "Event method already invoked");
			observedValue.set(10, notifyChange);
			assertEquals(notifyChange, eventMethodInvoked, () -> "Event method invoked");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getTestMethod().get().getName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}

	}

	@DisplayName("Test Serialization - Boolean")
	@ParameterizedTest
	@CsvSource({"true", "false"})	
	void testSerialization(boolean expected, @TempDir Path tempDir) {

		ObservedValue<Boolean> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Boolean> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Byte")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(byte expected, @TempDir Path tempDir) {

		ObservedValue<Byte> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Byte> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());
	}

	@DisplayName("Test Serialization - Char")
	@ParameterizedTest
	@CsvSource({"A", "T"})		
	void testSerialization(char expected, @TempDir Path tempDir) {

		ObservedValue<Character> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Character> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Short")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(short expected, @TempDir Path tempDir) {

		ObservedValue<Short> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Short> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Int")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(int expected, @TempDir Path tempDir) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Integer> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Long")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(long expected, @TempDir Path tempDir) {

		ObservedValue<Long> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Long> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Float")
	@ParameterizedTest
	@CsvSource({"65f"})		
	void testSerialization(float expected, @TempDir Path tempDir) {

		ObservedValue<Float> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Float> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Double")
	@ParameterizedTest
	@CsvSource({"65.55"})	
	void testSerialization(double expected, @TempDir Path tempDir) {

		ObservedValue<Double> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Double> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - String")
	@ParameterizedTest
	@CsvSource({"Test of string", "Another test"})		
	void testSerialization(String expected, @TempDir Path tempDir) {

		ObservedValue<String> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<String> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expected, value.get());

	}

	@DisplayName("Test Serialization - Object")
	@ParameterizedTest
	@CsvSource({"Paco, 44", "Lola, 41"})
	void testSerialization(String name, int age, @TempDir Path tempDir) {

		PersonObject expectedTestClass = new PersonObject(name, age);
		ObservedValue<PersonObject> observedValue = new ObservedValue<>(expectedTestClass);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<PersonObject> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expectedTestClass, value.get());
		
	}


	@DisplayName("Test Serialization - Array of int")
	@ParameterizedTest
	@ArraySources(arrays = {
		@ArraySource(array = {1, 2, 3, 4, 5, 6}),
		@ArraySource(array = {21, 34, 68}),
		@ArraySource(array = {72, 84, 78})
	})
	void testSerialization(int[] expected, @TempDir Path tempDir) {

		ObservedValue<int[]> observedValue = new ObservedValue<>(expected);
		serialiceToTempFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<int[]> value = deserialiceFromTempFile(ObservedValue.class, fileName, tempDir);

		assertTrue(Arrays.equals(expected, value.get()));
		
	}

	private static <T> void valueChangedTest(T oldValue, T newValue) {
		eventMethodInvoked = true;
		LOGGER.debug("Value changed event metod called. [oldValue: " + oldValue + ", newValue: " + newValue + "]");
	}
	
	private <T extends Serializable> void serialiceToTempFile(T value, String fileName, Path tempDir) {
		
		assumeTrue(tempDir != null);
		assumeTrue(fileName != null && !fileName.trim().isEmpty());
		
		File file = tempDir.resolve(fileName).toFile();
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
	
	private <T> T deserialiceFromTempFile(Class<T> cls, String fileName, Path tempDir) {
		
		assumeTrue(tempDir != null);
		assumeTrue(fileName != null && !fileName.trim().isEmpty());
		
		File file = tempDir.resolve(fileName).toFile();
		T value = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			try(ObjectInputStream objectInputStream	= new ObjectInputStream(fileInputStream)) {
				value = convertInstanceOfObject(objectInputStream.readObject(), cls);
			}
		} catch (Exception e) {
			assumeNoException(e);
		}
		
		return value;
	}
	
	private <T> T convertInstanceOfObject(Object obj, Class<T> cls) {
	    try {
	        return cls.cast(obj);
	    } catch(ClassCastException e) {
	        return null;
	    }
	}

}
