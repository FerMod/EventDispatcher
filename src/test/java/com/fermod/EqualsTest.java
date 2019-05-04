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
import org.junit.jupiter.api.extension.ExtendWith;

import com.fermod.contract.EqualsContract;
import com.fermod.extension.TimingExtension;
import com.fermod.testdata.serializable.PersonObject;
import com.fermod.util.RandomString;

/**
 * This class defines test of equality that the objects should pass.
 */
@DisplayName("Equals Test")
@ExtendWith({TimingExtension.class})
class EqualsTest {

	private static final Logger LOGGER = LogManager.getLogger(EqualsTest.class);
	
	static RandomString randString;
	
	@BeforeAll
	static void beforeAll() {
		randString = new RandomString(10, ThreadLocalRandom.current());
	}

	@AfterAll
	static void afterAll() {
	}

	@BeforeEach
	void beforeEach() {
	}

	@Nested
	@DisplayName("PersonObject Equals Test")
	class PersonObjectEqualsTest implements EqualsContract<PersonObject> {

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
