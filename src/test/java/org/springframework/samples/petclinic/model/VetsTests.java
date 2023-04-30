package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mohammad Saeid Nafar
 */
class VetsTests {

	private Vets vets;

	@BeforeEach
	void setUp() {
		vets = new Vets();
	}

	@Test
	@DisplayName("Test the vets getter")
	void testGetVetLists() {
		assertEquals(new ArrayList<>(), vets.getVetList());
	}

}
