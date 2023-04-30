package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mohammad Saeid Nafar
 */
class VisitTests {

	private Visit visit;

	@BeforeEach
	void setUp() {
		// Create new Visit instance before running each tests to avoid code redundancy
		visit = new Visit();
		visit.setDate(LocalDate.parse("2022-11-08"));
		visit.setDescription("Broken Leg");
		visit.setId(57631);
		visit.setPetId(17);
	}

	@Test
	@DisplayName("Tests the date field getter of a Visit object")
	void testGetDate() {
		// Get date of our Visit object and check if returns the correct date
		LocalDate testDate = visit.getDate();
		assertEquals(LocalDate.parse("2022-11-08"), testDate);
	}

	@Test
	@DisplayName("Tests the description field getter of a Visit object")
	void testGetDescription() {
		// Get description of our Visit object and check if returns the correct
		// description
		String testDescription = visit.getDescription();
		assertEquals("Broken Leg", testDescription);
	}

	@Test
	@DisplayName("Tests the ID field getter of a Visit object")
	void testGetId() {
		// Get ID of our Visit object and check if returns the correct ID
		int testId = visit.getId();
		assertEquals(57631, testId);
	}

	@Test
	@DisplayName("Tests the Pet ID field getter of a Visit object")
	void testGetPetId() {
		// Get Pet ID of our Visit object and check if returns the correct Pet ID
		int testPetId = visit.getPetId();
		assertEquals(17, testPetId);
	}

}
