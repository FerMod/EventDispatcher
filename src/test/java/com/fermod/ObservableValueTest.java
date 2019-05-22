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

/**
 * Class to test the correct behavior of the {@link ObservedValue} class.
 * 
 */
@ExtendWith({TimingExtension.class})
class ObservableValueTest {

	/** 
	 * Logger to print log messages to the console or to a file. 
	 */
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

	/**
	 * Test that unregisters correctly a listener.
	 * 
	 * @param value a value to test that unregisters correctly
	 * @param testInfo the instance of TestInfo that JUnit supplies that corresponds to the current test or container
	 * 
	 * @see TestInfo
	 */
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

	/**
	 * Test that unregisters all the previously registered listeners.
	 * 
	 * @param value a value to test that unregisters correctly
	 * @param testInfo the instance of TestInfo that JUnit supplies that corresponds to the current test or container
	 * 
	 * @see TestInfo
	 */
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
	

	/**
	 * Test the correct invocation of a inline method when a change of the observed variable happens.
	 * 
	 * @param expected the expected value
	 * @param value the new value
	 * @param testInfo the instance of TestInfo that JUnit supplies that corresponds to the current test or container
	 * 
	 * @see TestInfo
	 */
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

	/**
	 * Test the correct invocation of a referenced method when a change of  observed variable happens.
	 * 
	 * @param name the name of the person
	 * @param newName the new name of the person
	 * @param age the age of the person
	 */
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
	
	/**
	 * Test the equality of the objects of the original and the one given by the listener method.  
	 * 
	 * @param name the name of the person
	 * @param newName the new name of the person
	 * @param age the age of the person
	 */
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

	/**
	 * Test the events not firing when the new value is the same as the old value. 
	 * 
	 * @param value the new value of the observed value
	 * @param testInfo the instance of TestInfo that JUnit supplies that corresponds to the current test or container
	 * 
	 * @see TestInfo
	 */
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

	/**
	 * Test the correct behavior when enabling/disabling the notification when changing the variable value.
	 * 
	 * @param notifyChange a boolean that enables or disables the firing of the event 
	 * @param testInfo the instance of TestInfo that JUnit supplies that corresponds to the current test or container
	 * 
	 * @see TestInfo
	 */
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

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Boolean} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Boolean")
	@ParameterizedTest
	@CsvSource({"true", "false"})	
	void testSerialization(boolean expected, @TempDir Path tempDir) {

		ObservedValue<Boolean> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Boolean> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Byte} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Byte")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(byte expected, @TempDir Path tempDir) {

		ObservedValue<Byte> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Byte> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());
	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Character} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Char")
	@ParameterizedTest
	@CsvSource({"A", "T"})		
	void testSerialization(char expected, @TempDir Path tempDir) {

		ObservedValue<Character> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Character> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Short} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Short")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(short expected, @TempDir Path tempDir) {

		ObservedValue<Short> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Short> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Integer} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Int")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(int expected, @TempDir Path tempDir) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Integer> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}
	
	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Long} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Long")
	@ParameterizedTest
	@CsvSource({"65"})		
	void testSerialization(long expected, @TempDir Path tempDir) {

		ObservedValue<Long> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Long> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Float} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Float")
	@ParameterizedTest
	@CsvSource({"65f"})		
	void testSerialization(float expected, @TempDir Path tempDir) {

		ObservedValue<Float> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Float> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);
		
		assertEquals(expected, value.get());

	}
	
	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code Double} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Double")
	@ParameterizedTest
	@CsvSource({"65.55"})	
	void testSerialization(double expected, @TempDir Path tempDir) {

		ObservedValue<Double> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<Double> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expected, value.get());

	}


	/**
	 * Test the serialization of {@link ObservedValue} that holds a {@code String} type value.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - String")
	@ParameterizedTest
	@CsvSource({"Test of string", "Another test"})		
	void testSerialization(String expected, @TempDir Path tempDir) {

		ObservedValue<String> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<String> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expected, value.get());

	}


	/**
	 * Test the serialization of {@link ObservedValue} that holds an {@code Object} type value.
	 * 
	 * @param name the name of the person
	 * @param age the age of the person
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Object")
	@ParameterizedTest
	@CsvSource({"Paco, 44", "Lola, 41"})
	void testSerialization(String name, int age, @TempDir Path tempDir) {

		PersonObject expectedTestClass = new PersonObject(name, age);
		ObservedValue<PersonObject> observedValue = new ObservedValue<>(expectedTestClass);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<PersonObject> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);

		assertEquals(expectedTestClass, value.get());
		
	}

	/**
	 * Test the serialization of {@link ObservedValue} that holds an array of integer values.
	 * 
	 * @param expected the expected result of the test
	 * @param tempDir the path of the temporal directory that is resolved by JUnit
	 * 
	 * @see TempDir
	 */
	@DisplayName("Test Serialization - Array of int")
	@ParameterizedTest
	@ArraySources(arrays = {
		@ArraySource(array = {1, 2, 3, 4, 5, 6}),
		@ArraySource(array = {21, 34, 68}),
		@ArraySource(array = {72, 84, 78})
	})
	void testSerialization(int[] expected, @TempDir Path tempDir) {

		ObservedValue<int[]> observedValue = new ObservedValue<>(expected);
		serialiceToFile(observedValue, fileName, tempDir);
		
		@SuppressWarnings("unchecked")
		ObservedValue<int[]> value = deserialiceFromFile(ObservedValue.class, fileName, tempDir);

		assertTrue(Arrays.equals(expected, value.get()));
		
	}

	/**
	 * Prints a log a message, with the old and the current value. This method is used in the tests, that 
	 * is called by making references to it.
	 * 
	 * @param <T> the type of the value
	 * @param oldValue the value before the change
	 * @param newValue the current value
	 */
	private static <T> void valueChangedTest(T oldValue, T newValue) {
		eventMethodInvoked = true;
		LOGGER.debug("Value changed event metod called. [oldValue: " + oldValue + ", newValue: " + newValue + "]");
	}

	/**
	 * Serializes and stores a object in a file.
	 * 
	 * @param <T> the serializable object
	 * @param value the object that will be serialized and stored in the file
	 * @param fileName the file name, where the object will be stored
	 * @param dirPath the path to the directory containing the file
	 */
	private <T extends Serializable> void serialiceToFile(T value, String fileName, Path dirPath) {
		
		assumeTrue(dirPath != null);
		assumeTrue(fileName != null && !fileName.trim().isEmpty());
		
		File file = dirPath.resolve(fileName).toFile();
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

	/**
	 * Deserializes and returns an object stored in a file.
	 * 
	 * @param <T> the serializable object
	 * @param cls the {@code Class} object that represents the class or interface the instance
	 * @param fileName the file name, where the object will be stored in
	 * @param dirPath the path to the directory containing the file
	 * @return the object after being deserialized, that was stored in the file
	 */
	private <T extends Serializable> T deserialiceFromFile(Class<T> cls, String fileName, Path dirPath) {
		
		assumeTrue(dirPath != null);
		assumeTrue(fileName != null && !fileName.trim().isEmpty());
		
		File file = dirPath.resolve(fileName).toFile();
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
	
	/**
	 * Returns a instance by casting the passed object.
	 * 
	 * @param <T> the class instance
	 * @param obj the object to be cast 
	 * @param cls the {@code Class} object that represents the class or interface the instance
	 * @return the instance of the object
	 */
	private <T> T convertInstanceOfObject(Object obj, Class<T> cls) {
	    try {
	        return cls.cast(obj);
	    } catch(ClassCastException e) {
	        return null;
	    }
	}

}
