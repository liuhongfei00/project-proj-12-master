package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * @author Anaëlle Drai Laguéns
 */
public class NamedEntityTests {

	private NamedEntity namedEntity;

	private static final Integer ID = 2;

	private static final String NAME = "Test Name";

	@BeforeEach
	void setup() {
		// Initialize NameEntity object with name and id
		namedEntity = new NamedEntity();
		namedEntity.setName(NAME);
		namedEntity.setId(ID);
	}

	@Test
	@DisplayName("Test the name getter")
	void testGetName() {
		// Determine if the returned string matched the name previously set
		assertEquals(namedEntity.getName(), NAME);
	}

	@Test
	@DisplayName("Test the id getter")
	void testGetId() {
		// Determine if the returned id matches the id previously set
		assertEquals(namedEntity.getId(), ID);
	}

	@Test
	@DisplayName("Test the is new getter")
	void testIsNew() {
		// Test one of the superclass methods to make sure the inheritance works
		assertEquals(namedEntity.isNew(), false);
	}

	@Test
	@DisplayName("Test the to string method")
	void testToString() {
		// Test the to string method is properly overriden and returns the name
		assertEquals(namedEntity.toString(), NAME);
	}

}
