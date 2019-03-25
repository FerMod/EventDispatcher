package com.fermod;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fermod.extension.TimingExtension;

/**
 * This class defines test of equality that the objects should pass.
 */
@DisplayName("Equals Test")
@ExtendWith({TimingExtension.class})
class EqualsTest {

	@BeforeAll
	static void beforeAll() {
	}

	@AfterAll
	static void afterAll() {
	}

	@BeforeEach
	void beforeEach() {
	}

}
