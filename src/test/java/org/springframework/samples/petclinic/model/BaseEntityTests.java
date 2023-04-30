package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * @author Anaëlle Drai Laguéns
 */
public class BaseEntityTests {

	private BaseEntity baseEntity;

	private static final Integer ID = 1;

	@BeforeEach
	void setup() {
		// Create a baseEntity instance
		baseEntity = new BaseEntity();
	}

	@Test
	@DisplayName("Testing the id setter")
	void testGetId() {
		// Set the BaseEntity object ID and check it is retrieved correctly
		baseEntity.setId(ID);
		assertEquals(baseEntity.getId(), ID);
	}

	@Test
	@DisplayName("Testing is new when id is not null")
	void testIsNewWhenIdIsNotNull() {
		// Set the BaseEntity object ID and check isNew properly returns false
		baseEntity.setId(ID);
		assertEquals(baseEntity.isNew(), false);
	}

	@Test
	@DisplayName("Testing is new when id is null")
	void testIsNewWhenIdIsNull() {
		// Do not set the baseEntity object ID ans check isNew properly returns true
		assertEquals(baseEntity.isNew(), true);
	}

}
