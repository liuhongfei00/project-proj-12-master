package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * @author Anaëlle Drai Laguéns
 */
public class PersonTests {

	private Person person;

	// Person Data
	private static final String FIRST_NAME = "James";

	private static final String LAST_NAME = "Test";

	@BeforeEach
	void setup() {
		// Create a person instance and set the first name and last name
		person = new Person();
		person.setFirstName(FIRST_NAME);
		person.setLastName(LAST_NAME);
	}

	@Test
	@DisplayName("Test the first name getter")
	void testGetFirstName() {
		// Test the first name getter
		assertEquals(person.getFirstName(), FIRST_NAME);
	}

	@Test
	@DisplayName("Test the last name getter")
	void testGetLastName() {
		// Test the last name getter
		assertEquals(person.getLastName(), LAST_NAME);
	}

}
