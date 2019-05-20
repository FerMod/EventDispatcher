package com.fermod;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fermod.contract.EqualsContract;
import com.fermod.extension.TimingExtension;
import com.fermod.observer.ObservedValue;
import com.fermod.testdata.serializable.PersonObject;
import com.fermod.util.RandomString;

/**
 * This class defines test of equality that the objects should pass.
 */
@DisplayName("Equals Test")
@ExtendWith({TimingExtension.class})
class EqualsTest {

	private static final Logger LOGGER = LogManager.getLogger(EqualsTest.class);
	
	@AfterAll
	static void afterAll() {
	}

	@BeforeEach
	void beforeEach() {
	}
	
	@Nested
	@DisplayName("ObservedValue Equals Test")
	class ObservedValueEqualsTest implements EqualsContract<ObservedValue<?>> {

		@Override
		public ObservedValue<Integer> createValue() {	

			ObservedValue<Integer> observedValue = null;
			try {
				observedValue = createObservedValue(10);
			} catch (Exception e) {
				fail("Exception raised when creating the test data.", e);
			}

			return observedValue;
		}

		@Override
		public ObservedValue<Integer> createNotEqualValue() {	
			
			ObservedValue<Integer> observedValue = null;
			try {
				observedValue = createObservedValue(20);
			} catch (Exception e) {
				fail("Exception raised when creating the test data.", e);
			}

			return observedValue;
		}
		
		private ObservedValue<Integer> createObservedValue(int value) {
			return new ObservedValue<>(value);
		}

	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("PersonObject Equals Test")
	class PersonObjectEqualsTest implements EqualsContract<PersonObject> {
		
		RandomString randString;
		
		@BeforeAll
		void beforeAll() {
			randString = new RandomString(10, ThreadLocalRandom.current());
		}

		@Override
		public PersonObject createValue() {	

			PersonObject personObject = null;
			try {
				personObject = createRandomPersonObject();
			} catch (Exception e) {
				fail("Exception raised when creating the test data.", e);
			}

			return personObject;
		}

		@Override
		public PersonObject createNotEqualValue() {	
			
			PersonObject personObject = null;
			try {
				personObject = createRandomPersonObject();
			} catch (Exception e) {
				fail("Exception raised when creating the test data.", e);
			}

			return personObject;
		}

		private PersonObject createRandomPersonObject() {
			
			String randomString = randString.nextString();
			
			Random random = ThreadLocalRandom.current();
			int randomInt = random.nextInt(200);

			LOGGER.debug("Created PersonObject[name: " + randomString + ", age: " + randomInt + "]");
			
			return new PersonObject(randomString, randomInt);
		}

	}

}
